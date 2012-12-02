if(window.location.hostname!='anz.j.cn'&& window.location.hostname.substr(0,4) != 'ics.'&& window.location.hostname.indexOf("fenfen.com") > -1){
    var langtaojin_client_id="4";
    var ltj_phone="";
    var showorder="";
    try{
        langtaojin_client_id=sem_client_id;
        ltj_phone=switchPhone400;
        showorder=idorders;
    }catch(e){}
    document.write("<script src='http://track.langtaojin.com/tracking/referrer.js'></script>");
    document.write("<script src='http://track.langtaojin.com/tracking/stay.js'></script>");
}