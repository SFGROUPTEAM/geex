create or replace package ox_pack_common is

  -- Author  : sunbx
  -- Created : 2018-04-23 13:37:24
  -- Purpose : 公共的存储过程，视图和函数

  function f_GetCode(p_billPrefix varchar2 ,p_len number:=4) RETURN varchar2;
 
  function f_GetUUID return varchar2;
  
  function f_isNumber(i_str varchar2) return number ;
  function f_toNumber(i_str varchar2) return number ;
  
 
  type aryStr is table of varchar2(255) index by binary_integer;
  procedure splitStr(Str IN varchar2, sp char, ary OUT aryStr, Cnt OUT number);
end ox_pack_common;
/
create or replace package body ox_pack_common is


function f_GetCode(p_billPrefix varchar2 ,p_len number:=4) RETURN varchar2 as
v_date date;
v_serialNum number;
v_cnt number;
v_ret varchar2(30);
	
begin

	if( p_billPrefix is null or length(p_billPrefix)<=0) then
		return '';
	end if;

	select sysdate into v_date from dual;
	
	select count(1) into v_cnt from ox_billcode a where a.billprefix=p_billPrefix 
		and to_char(a.billDate,'yyyy-mm-dd')=to_char(v_date,'yyyy-mm-dd');
	if(v_cnt=0) then
		v_serialNum:=1;
		insert into ox_billcode(billprefix,billdate,serialnum) values(p_billPrefix,v_date,v_serialNum);
	else
		select serialNum into v_serialNum from ox_billcode a where a.billprefix=p_billPrefix and to_char(a.billDate,'yyyy-mm-dd')=to_char(v_date,'yyyy-mm-dd');
		v_serialNum:=v_serialNum+1;
		update ox_billcode a set a.serialNum=v_serialNum where a.billprefix=p_billPrefix and to_char(a.billDate,'yyyy-mm-dd')=to_char(v_date,'yyyy-mm-dd');
	end if;
  --commit;
	v_ret:= p_billPrefix || to_char(v_date,'yyyymmdd')|| lpad(to_char(v_serialNum),p_len,'0');
	return (v_ret);
end;

function f_GetUUID return varchar2 as
v_uuid char(32);
begin
  select lower(sys_guid()) into v_uuid from dual;
  return(v_uuid);
end;

function f_isNumber(i_str varchar2) return number as
v_number number;
begin
  select to_number(i_str) into v_number from dual;
  if v_number is not null then
    return 1;
  else
    return 0;
  end if;
EXCEPTION
  WHEN OTHERS
  THEN
    return 0;
end;

function f_toNumber(i_str varchar2) return number as
v_number number;
begin
  select to_number(i_str) into v_number from dual;
  if v_number is not null then
    return v_number;
  else
    return 0;
  end if;
EXCEPTION
  WHEN OTHERS
  THEN
    return 0;
end;


 procedure splitStr(Str IN varchar2, sp char, ary OUT aryStr, Cnt OUT number) as
    i     number;
    s     varchar2(15);
    c     char;
  begin
    i:=1;
    s:='';
    --p:=1;
    Cnt:=0;
    loop
      exit when i>Length(Str);
      c:=SubStr(Str,i,1);
      if c=sp or i=Length(Str) then
        if c=sp then
          Cnt:=Cnt+1;
          ary(Cnt):=s;
        else
          ary(0):=s || c;
        end if;
        s:='';
        --p:=i+1;
      else
        s:=s || c;
      end if;
      i:=i+1;
    end loop;
end;




end ox_pack_common;
/
