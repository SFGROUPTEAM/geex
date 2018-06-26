create or replace package OX_PACK_SUPPLIERA is

  -- Author  : ADMIN
  -- Created : 2018/4/24 17:40:13
  -- Purpose : 
  
  -- Public type declarations
  procedure login(p_userName varchar2,p_userPwd varchar2,p_retVal out number,p_retMsg out varchar2);

--�ҵ����
  procedure auditToSell(p_toSellId varchar2,p_auditUser varchar2,p_retVal out number,p_retMsg out varchar2);
  
--�ҵ�����
 procedure cancelToSell(p_toSellId varchar2,p_cancelUser varchar2,p_retVal out number,p_retMsg out varchar2);
  
end OX_PACK_SUPPLIERA;
/
create or replace package body OX_PACK_SUPPLIERA is

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



--�ҵ����
 procedure auditToSell(p_toSellId varchar2,p_auditUser varchar2,p_retVal out number,p_retMsg out varchar2 )
 as
 v_cnt number;
 v_now char(14);
 v_row_supplier_tosell ox_supplier_tosell%rowtype;
 msg varchar2(1000);
 err_msg exception;
 begin
     
     select to_char(sysdate,'yyyyMMddHH24miss') into v_now from dual;
     select count(*) into v_cnt from ox_supplier_tosell a where a.id=p_toSellId;
     if(v_cnt=0) then
        msg:='δ�ҵ�ָ����װ���ҵ���Ϣ';
        raise err_msg;
     end if;
     select a.* into v_row_supplier_tosell from ox_supplier_tosell a where a.id=p_toSellId;
     if(v_row_supplier_tosell.status>0) then
        msg:='װ���ҵ������Ѿ���˹��ˣ������ظ���ˡ�';
        raise err_msg;
     end if;
     
     
    
     
     update ox_supplier_tosell a 
     set a.totalnum =(select count(1) from ox_supplier_tosell_detail where tosellid=v_row_supplier_tosell.id)
     where a.id=v_row_supplier_tosell.id;
     
     for rec in(select a.*,b.supplierid,b.orderno from ox_supplier_tosell_detail a,ox_supplier_tosell b,ox_supplier c where a.tosellid=b.id 
       and b.supplierid=c.id and a.tosellid=v_row_supplier_tosell.id)
     loop
          
          
         --���װ���䶯��־
        insert into ox_game_equipment_log(EQUIPMENTID,CREATETIME,CREATEUSER,ORDERTYPE,ORDERNO	,QUANTITY	,
               PRICE,CURRENCYID,CURRENCYNAME) 
        values(rec.equipmentid,v_now,p_auditUser,1,rec.orderno,rec.quantity,rec.price,rec.currencyid,rec.currencyname);
         
      
        --���»����װ������¼
        insert into ox_supplier_tosell_equipment(SUPPLIERID,EQUIPMENTID, TOSELLID,EQUIPMENTNAME,TOSELLDETAILID,
                PRICE,CURRENCYID,CURRENCYNAME,QUANTITY)
         values(rec.supplierid,rec.equipmentid,rec.tosellid,rec.equipmentname,rec.id,rec.price,rec.currencyid,rec.currencyname,
         rec.quantity);
          
     end loop;
     
     
    
     --���¹ҵ�״̬
     update ox_supplier_tosell set status=1,audittime=v_now,audituser=p_auditUser 
     where id=v_row_supplier_tosell.id;
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

--�ҵ�����
 procedure cancelToSell(p_toSellId varchar2,p_cancelUser varchar2,p_retVal out number,p_retMsg out varchar2)
  as
 v_cnt number;
 v_now char(14);
 v_row_supplier_tosell ox_supplier_tosell%rowtype;
 --v_row_supplier_tosell_d ox_supplier_tosell_detail%rowtype;
 --v_row_supplier_tosell_equip ox_supplier_tosell_equipment%rowtype;
 --v_leftqty number;
 msg varchar2(1000);
 err_msg exception;
 --v_oldstockqty number(12,2);
 begin
     
     select to_char(sysdate,'yyyyMMddHH24miss') into v_now from dual;
     select count(*) into v_cnt from ox_supplier_tosell a where a.id=p_toSellId;
     if(v_cnt=0) then
        msg:='δ�ҵ�ָ����װ���ҵ���Ϣ';
        raise err_msg;
     end if;
     select a.* into v_row_supplier_tosell from ox_supplier_tosell a where a.id=p_toSellId;
     if(v_row_supplier_tosell.status=0) then
        msg:='װ���ҵ�������δ��ˣ����ܳ�����';
        raise err_msg;
     elsif(v_row_supplier_tosell.status=10) then
        msg:='װ���ҵ������Ѿ�������';
        raise err_msg;
     elsif(v_row_supplier_tosell.status=5) then
        msg:='װ���Ѿ�ȫ���۳������ܳ�����';
        raise err_msg;
     else
       null;
     end if;
     
     
     for rec in(select c.*,b.orderno from ox_supplier_tosell_detail a,ox_supplier_tosell b,
        ox_supplier_tosell_equipment c where a.tosellid=b.id 
       and a.id=c.toselldetailid and a.tosellid=v_row_supplier_tosell.id)
     loop
         if(rec.frozenquantity>0) then
            msg:='�ùҵ����н����������ڴ������ܳ�����';
            raise err_msg;  
         end if; 
         --���¹��ﳵ����
         update ox_player_cart a set a.status =6 where a.toselldetailid=rec.toselldetailid;
         
         if(rec.quantity-rec.selledquantity-rec.cancelquantity<0.0001) then --��ȫ������
            continue;
         end if;
         
        
         --���װ���䶯��־
        insert into ox_game_equipment_log(EQUIPMENTID,CREATETIME,CREATEUSER,ORDERTYPE,ORDERNO	,QUANTITY	,
               PRICE,CURRENCYID,CURRENCYNAME) 
        values(rec.equipmentid,v_now,p_cancelUser,11,rec.orderno,rec.quantity-rec.selledquantity-rec.cancelquantity,
          rec.price,rec.currencyid,rec.currencyname);
       
       
        --���»����װ������¼
         update  ox_supplier_tosell_equipment a set a.cancelquantity=rec.quantity-rec.selledquantity
          where a.toselldetailid=rec.toselldetailid;
     
     end loop;
     
     --���¹ҵ�״̬
     update ox_supplier_tosell set status=10,updatetime=v_now,updateuser=p_cancelUser ,
            remark=nvl(remark,'') || p_cancelUser || '����'
     where id=v_row_supplier_tosell.id;
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



end OX_PACK_SUPPLIERA;
/
