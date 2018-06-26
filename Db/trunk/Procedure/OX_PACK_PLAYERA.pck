create or replace package OX_PACK_PLAYERA is

  -- Author  : ADMIN
  -- Created : 2018/4/24 17:40:13
  -- Purpose : 
  
  -- Public type declarations
  procedure login(p_userName varchar2,p_userPwd varchar2,p_retVal out number,p_retMsg out varchar2);

--���ﳵ���ɶ���
  procedure generateOrderFromCart(p_playerId varchar2,p_createUser varchar2,p_retVal out number,p_retMsg out varchar2);
  
 
--ֱ�����ɶ������޹��ﳵ���룩
  procedure generateOrderDirect(p_playerId varchar2,p_toselldetailId varchar2,p_quantity number,
    p_retVal out number,p_retMsg out varchar2);
  
--��������ǰ
  procedure beforePayOrder(p_orderId varchar2,p_payuser varchar2,p_retVal out number,p_retMsg out varchar2);

--���������
  procedure afterPayOrder(p_orderId varchar2,p_paystatus varchar2,p_otherData varchar2,p_retVal out number,p_retMsg out varchar2);

end OX_PACK_PLAYERA;
/
create or replace package body OX_PACK_PLAYERA is

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



--���ﳵ���ɶ���
  procedure generateOrderFromCart(p_playerId varchar2,p_createUser varchar2,p_retVal out number,p_retMsg out varchar2)
 as
 v_cnt number;
 v_orderno varchar2(50);
 v_orderId char(32);
 v_sno number(10);
 v_row_player ox_player%rowtype;
 v_createuser varchar2(30);
 v_now char(14);

 msg varchar2(1000);
 err_msg exception;
 --v_oldstockqty number(12,2);
 begin
     
     select to_char(sysdate,'yyyyMMddHH24miss') into v_now from dual;
     --��֤������
     select count(*) into v_cnt from ox_player a where a.id=p_playerId;
     if(v_cnt=0) then
        msg:='ָ������Ҳ�����';
        raise err_msg;
     end if;
     select a.* into v_row_player from ox_player a where a.id=p_playerId;
     if(v_row_player.status<>1) then
        msg:='��Ч���';
        raise err_msg;
     end if;
     
     
     -----------------------------------
     select count(*) into v_cnt from ox_player_cart a where a.playerid=p_playerId and a.status=0;
     if(v_cnt=0) then
	      msg:='���ﳵ������Чװ�����޷����ɶ�����';
        raise err_msg;
     end if;
     v_createuser:=p_createuser;
     if(p_createuser is null or length(p_createuser)=0) then
        v_createuser:=v_row_player.username;
     end if;
     
     for rec in(select d.id gameid,d.name gamename from ox_player_cart a,ox_supplier_tosell_equipment b,
       ox_game_equipment c,ox_game_info d
       where a.toselldetailid=b.toselldetailid and a.equipmentid=b.equipmentid
       and b.equipmentid=c.id and c.gameid=d.id and a.playerid=p_playerId and a.status=0 and b.QUANTITY-SELLEDQUANTITY-CANCELQUANTITY>0
       group by d.id,d.name)
     loop
        v_orderId:=ox_pack_common.f_GetUUID();
        v_orderno:=ox_pack_common.f_GetCode('AB',10);
	
	      insert into ox_player_order(ID,ORDERNO,PLAYERID,GAMEID,GAMENAME,STATUS,CREATEUSER,CREATETIME)
	      values(v_orderId,v_orderno,p_playerId,rec.gameid,rec.gamename,0,v_createuser,v_now);
        v_sno:=1;
	      for rec1 in(select b.* ,a.purchasenum from ox_player_cart a,ox_supplier_tosell_equipment b,ox_game_equipment c,
		        ox_game_info d
		        where a.toselldetailid=b.toselldetailid and a.equipmentid=b.equipmentid
		              and b.equipmentid=c.id and c.gameid=d.id and a.playerid=p_playerId and a.status=0 
		              and b.QUANTITY-SELLEDQUANTITY-CANCELQUANTITY>0 and d.id=rec.gameid) loop
	           insert into ox_player_orderdetail(ID,ORDERID,LOTID,QUANTITY,sno)
	           values(ox_pack_common.f_GetUUID(),v_orderId,rec1.toselldetailid,rec1.purchasenum,to_char(v_sno));
             v_sno:=v_sno+1;
	      end loop;
        update ox_player_order t set t.totalnum=(select count(*) from ox_player_orderdetail where orderid=v_orderId)
        where t.id=v_orderid;

     end loop;
    
     --�����ﳵ
     delete from ox_player_cart a where a.playerid=p_playerId and a.status=0;
     
     
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

--ֱ�����ɶ������޹��ﳵ���룩
procedure generateOrderDirect(p_playerId varchar2,p_toselldetailId varchar2,p_quantity number,
    p_retVal out number,p_retMsg out varchar2)
as
 v_cnt number;
 v_orderno varchar2(50);
 v_orderId char(32);
 v_row_player ox_player%rowtype;
 v_now char(14);

 
 v_row_tosell_equipment ox_supplier_tosell_equipment%rowtype;
 v_row_game ox_game_info%rowtype; 
 msg varchar2(1000);
 err_msg exception;
 --v_oldstockqty number(12,2);
 begin
     
     select to_char(sysdate,'yyyyMMddHH24miss') into v_now from dual;
     --��֤������
     select count(*) into v_cnt from ox_player a where a.id=p_playerId;
     if(v_cnt=0) then
        msg:='ָ������Ҳ�����';
        raise err_msg;
     end if;
     select a.* into v_row_player from ox_player a where a.id=p_playerId;
     if(v_row_player.status<>1) then
        msg:='��Ч���';
        raise err_msg;
     end if;
     
     select count(*) into v_cnt from ox_supplier_tosell_equipment a where a.toselldetailid=p_toselldetailId;
     if(v_cnt=0) then
        msg:='��Чװ��';
        raise err_msg;
     end if;
     select a.* into v_row_tosell_equipment from ox_supplier_tosell_equipment a where a.toselldetailid=p_toselldetailId;
     if(v_row_tosell_equipment.quantity-v_row_tosell_equipment.selledquantity<0.0001) then
        msg:='��ǰ������װ��������';
        raise err_msg;
     elsif(v_row_tosell_equipment.quantity-v_row_tosell_equipment.selledquantity
        -v_row_tosell_equipment.cancelquantity<0.0001) then
        msg:='��ǰ������װ�����¼�';
        raise err_msg;
     end if;
     
     select count(*) into v_cnt from ox_game_info a,ox_supplier_tosell_equipment b,ox_game_equipment c
      where a.id=c.gameid and c.id=b.equipmentid and b.toselldetailid=p_toselldetailId;
       
     if(v_cnt=0) then
        msg:='ϵͳ���ݴ���';
        raise err_msg;
     end if;
     
     select a.* into v_row_game from ox_game_info a,ox_supplier_tosell_equipment b,ox_game_equipment c
      where a.id=c.gameid and c.id=b.equipmentid and b.toselldetailid=p_toselldetailId and rownum=1;
      
     v_orderId:=ox_pack_common.f_GetUUID();
     v_orderno:=ox_pack_common.f_GetCode('AS',10);
	
	   insert into ox_player_order(ID,ORDERNO,PLAYERID,GAMEID,GAMENAME,STATUS,CREATEUSER,CREATETIME)
	      values(v_orderId,v_orderno,p_playerId,v_row_game.id,v_row_game.name,0,v_row_player.username,v_now);
     
     insert into ox_player_orderdetail(ID,ORDERID,LOTID,QUANTITY,sno)
	       values(ox_pack_common.f_GetUUID(),v_orderId,p_toselldetailId,p_quantity,'1');

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


--��������ǰ
procedure beforePayOrder(p_orderId varchar2,p_payuser varchar2,p_retVal out number,p_retMsg out varchar2)
as
 v_cnt number;
 v_row_order ox_player_order%rowtype;
 v_now char(14);
 v_payuser varchar2(30);
 msg varchar2(1000);
 err_msg exception;
 --v_oldstockqty number(12,2);
 begin
     
     select to_char(sysdate,'yyyyMMddHH24miss') into v_now from dual;
     --��֤����
     select count(*) into v_cnt from ox_player_order a where a.id=p_orderId;
     if(v_cnt=0) then
        msg:='ָ���Ķ���������';
        raise err_msg;
     end if;
     select a.* into v_row_order from ox_player_order a where a.id=p_orderId;
     
     
     for rec in(select a.quantity orderquantity,b.* from ox_player_orderdetail a,
        ox_supplier_tosell_equipment b
         where a.lotid=b.toselldetailid and a.orderid=v_row_order.id)
     loop
        
        if(rec.quantity=rec.selledquantity) then
          msg:='װ��'|| rec.equipmentname ||'������';
          raise err_msg;
        elsif(rec.quantity=rec.selledquantity+rec.cancelquantity) then
          msg:='װ��'|| rec.equipmentname ||'���¼�';
          raise err_msg;
        elsif(rec.quantity=rec.selledquantity+rec.cancelquantity+rec.frozenquantity) then
          msg:='װ��'|| rec.equipmentname ||'���޴��';
          raise err_msg;
        elsif(rec.quantity-rec.selledquantity-rec.cancelquantity-rec.frozenquantity<rec.orderquantity) then
          msg:='װ��'|| rec.equipmentname ||'������㣬Ŀǰʣ��' || to_char(rec.quantity-rec.selledquantity-rec.cancelquantity-rec.frozenquantity) || '��';
          raise err_msg;
	      else
          null;
        end if;
        update ox_supplier_tosell_equipment a set a.frozenquantity=a.frozenquantity+rec.orderquantity where
          a.toselldetailid=rec.toselldetailid and a.equipmentid=rec.equipmentid;
     end loop;
     v_payuser:=p_payuser;
     if(p_payuser is null or length(p_payuser)=0) then
       v_payuser:=v_row_order.createuser;
     end if;
     update ox_player_order a set a.status=5,a.payuser=v_payuser,a.paytime=v_now where a.id=v_row_order.id;
    
     
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

procedure afterPayOrder(p_orderId varchar2,p_paystatus varchar2,p_otherData varchar2,p_retVal out number,p_retMsg out varchar2)
as
 v_cnt number;
 v_row_order ox_player_order%rowtype;
 v_now char(14);
 v_flag varchar2(2);--��������ȫ���۳���־
 msg varchar2(1000);
 err_msg exception;
 --v_oldstockqty number(12,2);
 begin
     
     select to_char(sysdate,'yyyyMMddHH24miss') into v_now from dual;
     --��֤����
     select count(*) into v_cnt from ox_player_order a where a.id=p_orderId;
     if(v_cnt=0) then
        msg:='ָ���Ķ���������';
        raise err_msg;
     end if;
     select a.* into v_row_order from ox_player_order a where a.id=p_orderId;
     
     
     for rec in(select a.quantity orderquantity,b.* from ox_player_orderdetail a,
        ox_supplier_tosell_equipment b
         where a.lotid=b.toselldetailid and a.orderid=v_row_order.id)
     loop
        
        if(p_paystatus='00') then --����ɹ�
          update ox_supplier_tosell_equipment a set a.frozenquantity=a.frozenquantity-rec.orderquantity,
            a.selledquantity=a.selledquantity+rec.orderquantity where
            a.toselldetailid=rec.toselldetailid and a.equipmentid=rec.equipmentid;
          
          --�����ҵ��ѹ�װ��
          select count(*) into v_cnt from ox_player_equipment a where a.equipmentid=rec.equipmentid;
          if(v_cnt=0) then
            insert into  ox_player_equipment(PLAYERID,EQUIPMENTID,EQUIPMENTNAME,QUANTITY,CREATETIME)
             values(v_row_order.playerid,rec.equipmentid,rec.equipmentname,rec.orderquantity,v_now);
          else
            update ox_player_equipment a set a.quantity=nvl(a.quantity,0)+rec.orderquantity where 
            a.playerid=v_row_order.playerid and a.equipmentid=rec.equipmentid;
          end if;
          --���װ���䶯��־
          insert into ox_game_equipment_log(EQUIPMENTID,CREATETIME,CREATEUSER,ORDERTYPE,ORDERNO	,QUANTITY	,
               PRICE,CURRENCYID,CURRENCYNAME) 
          values(rec.equipmentid,v_now,'system',2,v_row_order.orderno,rec.orderquantity,rec.price,rec.currencyid,rec.currencyname);
         
          
        else
          update ox_supplier_tosell_equipment a set a.frozenquantity=a.frozenquantity-rec.orderquantity
             where
            a.toselldetailid=rec.toselldetailid and a.equipmentid=rec.equipmentid;
        end if; 
     end loop;
     --���²ɹ�����״̬
     if(p_paystatus='00') then --����ɹ�
       update ox_player_order a set a.status=10,a.payvoucherno=p_otherData,
       a.paytime=v_now where a.id=v_row_order.id;
     else --����ʧ��
        update ox_player_order a set a.status=6,
          a.paytime=v_now where a.id=v_row_order.id;
     end if;
     --���¹ҵ����� ��鱾�����µ���ϸ��Ӧ�Ĺ������Ƿ���ȫ���۳�
     if(p_paystatus='00') then
        for rec in(select distinct(b.tosellid) tosellid from ox_player_orderdetail a,
          ox_supplier_tosell_equipment b where a.lotid=b.toselldetailid and a.orderid=p_orderId)
        loop
           v_flag:='1';
           for rec1 in(select a.* from ox_supplier_tosell_equipment a where a.tosellid=rec.tosellid) loop
              if(rec1.quantity-rec1.selledquantity>0.0001) then
                  v_flag:='0';
                  exit;
              end if;
           end loop;
           if(v_flag='1') then
              update ox_supplier_tosell a set a.status=5 where a.id=rec.tosellid and a.status=1;
           end if;
        end loop;
     end if;
     ---------------------
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

end OX_PACK_PLAYERA;
/
