create or replace package OX_PACK_SUPPLIERB is

  -- Author  : ADMIN
  -- Created : 2018/4/24 17:40:13
  -- Purpose : 
  
  -- Public type declarations
  procedure login(p_userName varchar2,p_userPwd varchar2,p_retVal out number,p_retMsg out varchar2);

--购物车生成订单
  procedure generateOrderFromCart(p_supplierUserId varchar2,p_createUser varchar2,p_retVal out number,p_retMsg out varchar2);
  
 
--直接生成订单（无购物车参与）
  procedure generateOrderDirect(p_supplierUserId varchar2,p_toselldetailId varchar2,p_quantity number,
    p_retVal out number,p_retMsg out varchar2);
  
--订单付款前
  procedure beforePayOrder(p_orderId varchar2,p_payuser varchar2,p_retVal out number,p_retMsg out varchar2);

--订单付款后
  procedure afterPayOrder(p_orderId varchar2,p_paystatus varchar2,p_otherData varchar2,p_retVal out number,p_retMsg out varchar2);

end OX_PACK_SUPPLIERB;
/
create or replace package body OX_PACK_SUPPLIERB is

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



--购物车生成订单
  procedure generateOrderFromCart(p_supplierUserId varchar2,p_createUser varchar2,p_retVal out number,p_retMsg out varchar2)
 as
 v_cnt number;
 v_orderno varchar2(50);
 v_orderId char(32);
 v_sno number(10);
 v_row_supplier_user ox_supplier_user%rowtype;
 v_row_supplier ox_supplier%rowtype;
 v_createuser varchar2(30);
 v_now char(14);

 msg varchar2(1000);
 err_msg exception;
 --v_oldstockqty number(12,2);
 begin
     
     select to_char(sysdate,'yyyyMMddHH24miss') into v_now from dual;
     --验证供应商身份
     select count(*) into v_cnt from ox_supplier_user a where a.id=p_supplierUserId;
     if(v_cnt=0) then
        msg:='指定的供应商用户不存在';
        raise err_msg;
     end if;
     select a.* into v_row_supplier_user from ox_supplier_user a where a.id=p_supplierUserId;
     if(v_row_supplier_user.state<>1) then
        msg:='当前的游戏供应商操作员已被禁用，无法生成订单';
        raise err_msg;
     end if;

     select count(*) into v_cnt from ox_supplier a where a.id=v_row_supplier_user.supplierid;
     if(v_cnt=0) then
        msg:='指定的供应商不存在';
        raise err_msg;
     end if;
     select a.* into v_row_supplier from ox_supplier a where a.id=v_row_supplier_user.supplierid;
      if(v_row_supplier.status<>1) then
        msg:='当前的游戏供应商尚未审核通过，无法生成订单';
        raise err_msg;
     end if;
     if(v_row_supplier.state<>1) then
        msg:='当前的游戏供应商已被禁用，无法生成订单';
        raise err_msg;
     end if;

     
     -----------------------------------
     select count(*) into v_cnt from ox_supplier_cart a where a.supplieruserid=p_supplierUserId and a.status=0;
     if(v_cnt=0) then
	      msg:='购物车中无有效装备，无法生成订单。';
        raise err_msg;
     end if;
     v_createuser:=p_createuser;
     if(p_createuser is null or length(p_createuser)=0) then
        v_createuser:=v_row_supplier_user.username;
     end if;
     
     for rec in(select d.id gameid,d.name gamename from ox_supplier_cart a,ox_player_tosell_equipment b,ox_game_equipment c,
       ox_game_info d
       where a.toselldetailid=b.toselldetailid and a.equipmentid=b.equipmentid
       and b.equipmentid=c.id and c.gameid=d.id and a.supplieruserid=p_supplierUserId and a.status=0 and b.QUANTITY-SELLEDQUANTITY-CANCELQUANTITY>0
       group by d.id,d.name)
     loop
        v_orderId:=ox_pack_common.f_GetUUID();
        v_orderno:=ox_pack_common.f_GetCode('BP',10);
	
	      insert into ox_supplier_order(ID,ORDERNO,SUPPLIERID,GAMEID,GAMENAME,STATUS,CREATEUSER,CREATETIME)
	        values(v_orderId,v_orderno,v_row_supplier.id,rec.gameid,rec.gamename,0,v_createuser,v_now);
        v_sno:=1;
	      for rec1 in(select b.* ,a.purchasenum from ox_supplier_cart a,ox_player_tosell_equipment b,ox_game_equipment c,
		        ox_game_info d
		        where a.toselldetailid=b.toselldetailid and a.equipmentid=b.equipmentid
		              and b.equipmentid=c.id and c.gameid=d.id and a.supplieruserid=p_supplierUserId and a.status=0 
		              and b.QUANTITY-SELLEDQUANTITY-CANCELQUANTITY>0 and d.id=rec.gameid) loop
	          
            insert into ox_supplier_orderdetail(ID,ORDERID,LOTID,QUANTITY,sno)
	           values(ox_pack_common.f_GetUUID(),v_orderId,rec1.toselldetailid,rec1.purchasenum,to_char(v_sno));
             v_sno:=v_sno+1;
	      end loop;
        update ox_supplier_order t set t.totalnum=(select count(*) from ox_supplier_orderdetail where orderid=v_orderId),
	        t.totalamount=(select sum(a.quantity*b.price) from ox_supplier_orderdetail a,ox_player_tosell_equipment b where a.lotid=b.toselldetailid 
	        and a.orderid=v_orderId)
	        where t.id=v_orderId;
	
     end loop;
    
     --清理购物车
     delete from ox_supplier_cart a where a.supplieruserid=p_supplierUserId and a.status=0;
     
     
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

--直接生成订单（无购物车参与）
procedure generateOrderDirect(p_supplierUserId varchar2,p_toselldetailId varchar2,p_quantity number,
    p_retVal out number,p_retMsg out varchar2)
as
 v_cnt number;
 v_orderno varchar2(50);
 v_orderId char(32);
 v_row_supplier_user ox_supplier_user%rowtype;
 v_row_supplier ox_supplier%rowtype;
 v_now char(14);

 
 v_row_tosell_equipment ox_player_tosell_equipment%rowtype;
 v_row_game ox_game_info%rowtype; 
 msg varchar2(1000);
 err_msg exception;
 --v_oldstockqty number(12,2);
 begin
     
     select to_char(sysdate,'yyyyMMddHH24miss') into v_now from dual;
    --验证供应商身份
     select count(*) into v_cnt from ox_supplier_user a where a.id=p_supplierUserId;
     if(v_cnt=0) then
        msg:='指定的供应商用户不存在';
        raise err_msg;
     end if;
     select a.* into v_row_supplier_user from ox_supplier_user a where a.id=p_supplierUserId;
     if(v_row_supplier_user.state<>1) then
        msg:='当前的游戏供应商操作员已被禁用，无法生成订单';
        raise err_msg;
     end if;

     select count(*) into v_cnt from ox_supplier a where a.id=v_row_supplier_user.supplierid;
     if(v_cnt=0) then
        msg:='指定的供应商不存在';
        raise err_msg;
     end if;
     select a.* into v_row_supplier from ox_supplier a where a.id=v_row_supplier_user.supplierid;
      if(v_row_supplier.status<>1) then
        msg:='当前的游戏供应商尚未审核通过，无法生成订单';
        raise err_msg;
     end if;
     if(v_row_supplier.state<>1) then
        msg:='当前的游戏供应商已被禁用，无法生成订单';
        raise err_msg;
     end if;

     
     select count(*) into v_cnt from ox_player_tosell_equipment a where a.toselldetailid=p_toselldetailId;
     if(v_cnt=0) then
        msg:='无效装备';
        raise err_msg;
     end if;
     select a.* into v_row_tosell_equipment from ox_player_tosell_equipment a where a.toselldetailid=p_toselldetailId;
     if(v_row_tosell_equipment.quantity-v_row_tosell_equipment.selledquantity=0
       ) then
        msg:='当前待购买装备已售完';
        raise err_msg;
     elsif(v_row_tosell_equipment.quantity-v_row_tosell_equipment.selledquantity-v_row_tosell_equipment.cancelquantity=0
       ) then
        msg:='当前待购买装备已下架';
        raise err_msg;
     end if;
     
     select count(*) into v_cnt from ox_game_info a,ox_player_tosell_equipment b,ox_game_equipment c
      where a.id=c.gameid and c.id=b.equipmentid and b.toselldetailid=p_toselldetailId;
       
     if(v_cnt=0) then
        msg:='系统数据错误';
        raise err_msg;
     end if;
     
     select a.* into v_row_game from ox_game_info a,ox_player_tosell_equipment b,ox_game_equipment c
      where a.id=c.gameid and c.id=b.equipmentid and b.toselldetailid=p_toselldetailId and rownum=1;
      
     v_orderId:=ox_pack_common.f_GetUUID();
     v_orderno:=ox_pack_common.f_GetCode('BS',10);
	
	   insert into ox_supplier_order(ID,ORDERNO,SUPPLIERID,GAMEID,GAMENAME,STATUS,CREATEUSER,CREATETIME)
	      values(v_orderId,v_orderno,v_row_supplier.id,v_row_game.id,v_row_game.name,0,v_row_supplier_user.username,v_now);
     
     insert into ox_supplier_orderdetail(ID,ORDERID,LOTID,QUANTITY,sno)
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


--订单付款前
procedure beforePayOrder(p_orderId varchar2,p_payuser varchar2,p_retVal out number,p_retMsg out varchar2)
as
 v_cnt number;
 v_row_order ox_supplier_order%rowtype;
 v_row_balance ox_supplier_balance%rowtype;
 v_now char(14);
 v_payuser varchar2(30);
 msg varchar2(1000);
 err_msg exception;
 --v_oldstockqty number(12,2);
 begin
     
     select to_char(sysdate,'yyyyMMddHH24miss') into v_now from dual;
     --验证订单
     select count(*) into v_cnt from ox_supplier_order a where a.id=p_orderId;
     if(v_cnt=0) then
        msg:='指定的订单不存在';
        raise err_msg;
     end if;
     select a.* into v_row_order from ox_supplier_order a where a.id=p_orderId;
     
     --判断供应商是否有足够的游宝币
     select count(*) into v_cnt from ox_supplier_balance t where t.supplierid=v_row_order.supplierid;
     if(v_cnt=0) then
	      msg:='当前游戏供应商没有游宝币，无法支付订单';
        raise err_msg;
     end if;
     select t.* into v_row_balance from ox_supplier_balance t where t.supplierid=v_row_order.supplierid;
     if(v_row_balance.quantity-v_row_balance.frozenquantity<v_row_order.totalamount) then
	      msg:='当前可用游宝币数量'|| to_char(v_row_balance.quantity-v_row_balance.frozenquantity) || '不够支付订单金额（'|| to_char(v_row_order.totalamount) || '），无法支付订单';
        raise err_msg;
     end if;
     -------------------------------------------
     for rec in(select a.quantity orderquantity,b.* from ox_supplier_orderdetail a,
        ox_player_tosell_equipment b
         where a.lotid=b.toselldetailid and a.orderid=v_row_order.id)
     loop
        
        if(rec.quantity=rec.selledquantity) then
          msg:='装备'|| rec.equipmentname ||'已回购完毕';
          raise err_msg;
        elsif(rec.quantity=rec.selledquantity+rec.cancelquantity) then
          msg:='装备'|| rec.equipmentname ||'已下架';
          raise err_msg;
        elsif(rec.quantity=rec.selledquantity+rec.cancelquantity+rec.frozenquantity) then
          msg:='装备'|| rec.equipmentname ||'已无存货';
          raise err_msg;
        elsif(rec.quantity-rec.selledquantity-rec.cancelquantity-rec.frozenquantity<rec.orderquantity) then
          msg:='装备'|| rec.equipmentname ||'存货不足，目前剩余' || to_char(rec.quantity-rec.selledquantity-rec.cancelquantity-rec.frozenquantity) || '个';
          raise err_msg;
	else
          null;
        end if;
        update ox_player_tosell_equipment a set a.frozenquantity=a.frozenquantity+rec.orderquantity where
          a.toselldetailid=rec.toselldetailid and a.equipmentid=rec.equipmentid;
     end loop;
     v_payuser:=p_payuser;
     if(p_payuser is null or length(p_payuser)=0) then
       v_payuser:=v_row_order.createuser;
     end if;
     
    
     update ox_supplier_balance a set a.frozenquantity=nvl(a.frozenquantity,0)+v_row_order.totalamount where a.supplierid=v_row_order.supplierid;
     update ox_supplier_order a set a.status=5,a.payuser=v_payuser,a.paytime=v_now where a.id=v_row_order.id;

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
 v_row_order ox_supplier_order%rowtype;
 v_row_player_balance ox_player_balance%rowtype;
 v_row_supplier_balance ox_supplier_balance%rowtype;
 v_now char(14);

 msg varchar2(1000);
 err_msg exception;
 --v_oldstockqty number(12,2);
 begin
     
     select to_char(sysdate,'yyyyMMddHH24miss') into v_now from dual;
     --验证订单
     select count(*) into v_cnt from ox_supplier_order a where a.id=p_orderId;
     if(v_cnt=0) then
        msg:='指定的订单不存在';
        raise err_msg;
     end if;
     select a.* into v_row_order from ox_supplier_order a where a.id=p_orderId;
     
     
     for rec in(select a.quantity orderquantity,b.orderno,c.* from ox_supplier_orderdetail a,ox_supplier_order b,
        ox_player_tosell_equipment c
         where a.orderid=b.id and a.lotid=c.toselldetailid and a.orderid=v_row_order.id)
     loop
        
        if(p_paystatus='00') then --付款成功
	        --更新玩家待售装备
          update ox_player_tosell_equipment a set a.frozenquantity=a.frozenquantity-rec.orderquantity,
            a.selledquantity=a.selledquantity+rec.orderquantity where
            a.toselldetailid=rec.toselldetailid and a.equipmentid=rec.equipmentid;
          
	        --添加或更新玩家财富表
	        --添加玩家财富流水
	        select count(*) into v_cnt from ox_player_balance t where t.playerid=rec.playerid;
	        if(v_cnt>0) then
	          insert into ox_player_balance(playerid,quantity,updatetime) values(rec.playerid,rec.orderquantity,v_now);
	          insert into ox_player_balance_log(id,playerid,oldquantity,quantity,type,relatedid,createtime,remark)
	            values(ox_pack_common.f_GetUUID(),rec.playerid,0,rec.orderquantity,1,rec.orderno,v_now,'售出装备获得');
	        else
	          select * into v_row_player_balance from ox_player_balance t where t.playerid=rec.playerid;
	          update ox_player_balance a set a.quantity=a.quantity+rec.orderquantity,a.updatetime=v_now where a.playerid=rec.playerid;
	          insert into ox_player_balance_log(id,playerid,oldquantity,quantity,type,relatedid,createtime,remark)
	           values(ox_pack_common.f_GetUUID(),rec.playerid,v_row_player_balance.quantity,rec.orderquantity,1,rec.orderno,v_now,'售出装备获得');
	        end if;
	 
	  

          --更新供应商已回购装备
          select count(*) into v_cnt from ox_supplier_equipment a where a.equipmentid=rec.equipmentid;
          if(v_cnt=0) then
            insert into  ox_supplier_equipment(SUPPLIERID,EQUIPMENTID,EQUIPMENTNAME,QUANTITY,CREATETIME)
             values(v_row_order.supplierid,rec.equipmentid,rec.equipmentname,rec.orderquantity,v_now);
          else
            update ox_supplier_equipment a set a.quantity=nvl(a.quantity,0)+rec.orderquantity where 
            a.supplierid=v_row_order.supplierid and a.equipmentid=rec.equipmentid;
          end if;



          --添加装备变动日志
          insert into ox_game_equipment_log(EQUIPMENTID,CREATETIME,CREATEUSER,ORDERTYPE,ORDERNO	,QUANTITY	,
               PRICE,CURRENCYID,CURRENCYNAME) 
          values(rec.equipmentid,v_now,'system',4,v_row_order.orderno,rec.orderquantity,rec.price,'00000000','');
         
        else
           update ox_player_tosell_equipment a set a.frozenquantity=a.frozenquantity-rec.orderquantity
             where a.toselldetailid=rec.toselldetailid and a.equipmentid=rec.equipmentid;
        end if; 
     end loop;
     if(p_paystatus='00') then --付款成功
      
	     --更新卖家财富表
	     select count(*) into v_cnt from ox_supplier_balance t where t.supplierid=v_row_order.supplierid;
	     if(v_cnt<=0) then
	         msg:='系统数据出现异常';
           raise err_msg;
	     end if;
    
	     select t.* into v_row_supplier_balance from ox_supplier_balance t where t.supplierid=v_row_order.supplierid;
	     update ox_supplier_balance a set a.frozenquantity=nvl(a.frozenquantity,0)-v_row_order.totalamount,a.quantity=a.quantity-v_row_order.totalamount
	       where a.supplierid=v_row_order.supplierid;
	     --添加卖家财富流水记录
	     insert into ox_supplier_balance_log(id,supplierid,oldquantity,quantity,type,relatedid,createtime,remark)
	       values(ox_pack_common.f_GetUUID(),v_row_order.supplierid,v_row_supplier_balance.quantity,v_row_order.totalamount,2,v_row_order.orderno,v_now,'装备回购');

	     --更新供应商回购订单
       update ox_supplier_order a set a.status=10,a.payvoucherno=p_otherData,a.paytime=v_now where a.id=v_row_order.id;

     else --付款失败
	      --更新买家财富表
        update ox_supplier_balance a set a.frozenquantity=nvl(a.frozenquantity,0)-v_row_order.totalamount
	       where a.supplierid=v_row_order.supplierid;
	      --更新供应商回购订单
        update ox_player_order a set a.status=6,a.paytime=v_now where a.id=v_row_order.id;

     end if;
     
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

end OX_PACK_SUPPLIERB;
/
