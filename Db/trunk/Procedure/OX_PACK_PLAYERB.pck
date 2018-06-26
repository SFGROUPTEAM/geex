create or replace package OX_PACK_PLAYERB is

  -- Author  : ADMIN
  -- Created : 2018/4/24 17:40:13
  -- Purpose : 
  
  -- Public type declarations
  procedure login(p_userName varchar2,p_userPwd varchar2,p_retVal out number,p_retMsg out varchar2);

--挂单审核
  procedure auditToSell(p_toSellId varchar2,p_auditUser varchar2,p_retVal out number,p_retMsg out varchar2);
  
--挂单撤销
 procedure cancelToSell(p_toSellId varchar2,p_cancelUser varchar2,p_retVal out number,p_retMsg out varchar2);
  
end OX_PACK_PLAYERB;
/
create or replace package body OX_PACK_PLAYERB is

procedure login(p_userName varchar2,p_userPwd varchar2,p_retVal out number,p_retMsg out varchar2)
as
v_cnt number;
msg varchar2(1000);
err_msg exception;
begin
      select count(*)into v_cnt from dual;
       p_retMsg:='';
       p_retVal:=1;
      commit;
exception 
  when err_msg then
     p_retVal :=0;
     p_retMsg :=msg;
     rollback;
  when others then
     p_retVal :=0;
     p_retMsg := SUBSTR(SQLERRM, 1, 1000);
     rollback;
end;



--挂单审核
 procedure auditToSell(p_toSellId varchar2,p_auditUser varchar2,p_retVal out number,p_retMsg out varchar2 )
 as
 v_cnt number;
 v_now char(14);
 v_row_player_tosell ox_player_tosell%rowtype;
 msg varchar2(1000);
 err_msg exception;
 --v_oldstockqty number(12,2);
 begin
     
     select to_char(sysdate,'yyyyMMddHH24miss') into v_now from dual;
     select count(*) into v_cnt from ox_player_tosell a where a.id=p_toSellId;
     if(v_cnt=0) then
        msg:='未找到指定的装备挂单信息';
        raise err_msg;
     end if;
     select a.* into v_row_player_tosell from ox_player_tosell a where a.id=p_toSellId;
     if(v_row_player_tosell.status>0) then
        msg:='装备挂单数据已经审核过了，不能重复审核。';
        raise err_msg;
     end if;
     
     
    
     
     update ox_player_tosell a 
     set a.totalnum =(select count(1) from ox_player_tosell_detail where tosellid=v_row_player_tosell.id)
     where a.id=v_row_player_tosell.id;
     
     for rec in(select a.*,b.playerid,b.orderno from ox_player_tosell_detail a,ox_player_tosell b,ox_player c where a.tosellid=b.id 
       and b.playerid=c.id and a.tosellid=v_row_player_tosell.id)
     loop
          
          
         --添加装备变动日志
        insert into ox_game_equipment_log(EQUIPMENTID,CREATETIME,CREATEUSER,ORDERTYPE,ORDERNO	,QUANTITY	,
               PRICE,CURRENCYID,CURRENCYNAME) 
        values(rec.equipmentid,v_now,p_auditUser,3,rec.orderno,rec.quantity,rec.price,'00000000','');
         
      
        --更新或添加装备库存记录
        insert into ox_player_tosell_equipment(PLAYERID,EQUIPMENTID, TOSELLID,EQUIPMENTNAME,TOSELLDETAILID,
                PRICE,QUANTITY)
         values(rec.playerid,rec.equipmentid,rec.tosellid,rec.equipmentname,rec.id,rec.price,rec.quantity);
          
     end loop;
     
     
    
     --更新上架单状态
     update ox_player_tosell set status=1,audittime=v_now,audituser=p_auditUser 
     where id=v_row_player_tosell.id;
     p_retVal:=1;
     p_retMsg:='ok';
     commit;
exception 
  when err_msg then
     p_retVal :=0;
     p_retMsg :=msg;
     rollback;
  when others then
          p_retVal :=0;
          p_retMsg := SUBSTR(SQLERRM, 1, 1000);
          rollback;
end;

--挂单撤销
 procedure cancelToSell(p_toSellId varchar2,p_cancelUser varchar2,p_retVal out number,p_retMsg out varchar2)
  as
 v_cnt number;
 v_now char(14);
 v_row_player_tosell ox_player_tosell%rowtype;
 msg varchar2(1000);
 err_msg exception;

 begin
     
     select to_char(sysdate,'yyyyMMddHH24miss') into v_now from dual;
     select count(*) into v_cnt from ox_player_tosell a where a.id=p_toSellId;
     if(v_cnt=0) then
        msg:='未找到指定的装备挂单信息';
        raise err_msg;
     end if;
     select a.* into v_row_player_tosell from ox_player_tosell a where a.id=p_toSellId;
     if(v_row_player_tosell.status=0) then
        msg:='装备挂单数据尚未审核，不能撤销。';
        raise err_msg;
     elsif(v_row_player_tosell.status=10) then
        msg:='装备挂单数据已经撤销。';
        raise err_msg;
     elsif(v_row_player_tosell.status=5) then
        msg:='装备已经全部售出，不能撤销。';
        raise err_msg;
     else
       null;
     end if;
     
     
     for rec in(select c.*,b.orderno from ox_player_tosell_detail a,ox_player_tosell b,
        ox_player_tosell_equipment c where a.tosellid=b.id 
       and a.id=c.toselldetailid and a.tosellid=v_row_player_tosell.id)
     loop
         if(rec.frozenquantity>0) then
            msg:='该挂单下有交易数据正在处理，不能撤销。';
            raise err_msg;  
         end if; 
         if(rec.quantity=rec.selledquantity+rec.cancelquantity) then
            continue;
         end if;
          
         --添加装备变动日志
        insert into ox_game_equipment_log(EQUIPMENTID,CREATETIME,CREATEUSER,ORDERTYPE,ORDERNO	,QUANTITY	,
               PRICE,CURRENCYID,CURRENCYNAME) 
        values(rec.equipmentid,v_now,p_cancelUser,13,rec.orderno,rec.quantity-rec.selledquantity-rec.cancelquantity,
          rec.price,'00000000','');
       
       
        --更新或添加装备库存记录
         update  ox_player_tosell_equipment a set a.cancelquantity=rec.quantity-rec.selledquantity
          where a.toselldetailid=rec.toselldetailid;
      
          
     end loop;
     
     
    
     --更新挂卖单状态
     update ox_player_tosell set status=10,updatetime=v_now,updateuser=p_cancelUser ,
       remark=nvl(remark,'') || p_cancelUser || '撤销'
     where id=v_row_player_tosell.id;
     p_retVal:=1;
     p_retMsg:='ok';
     commit;
exception 
  when err_msg then
     p_retVal :=0;
     p_retMsg :=msg;
     rollback;
  when others then
          p_retVal :=0;
          p_retMsg := SUBSTR(SQLERRM, 1, 1000);
          rollback;
end;



end OX_PACK_PLAYERB;
/
