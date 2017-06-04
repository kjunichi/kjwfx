function viewRecords() {
// kjwfxを呼び出す
  try {
    xmlReq = new ActiveXObject("Microsoft.XMLHTTP");
  } catch(e) {
    xmlReq = new XMLHttpRequest();
  }
 xmlReq.onreadystatechange = function() {
     var msg = document.getElementById("FXRECORD");
     if (xmlReq.readyState == 4) {
      if (xmlReq.status == 200) {
        msg.innerHTML = xmlReq.responseText;
      } else {
        msg.innerHTML = "通信に失敗しました。";
      }
    } else {
      msg.innerHTML = "通信中…";
    }
  }
  xmlReq.open("GET","/kjwfx",true);
xmlReq.send(null);

//document.getElementById("FXRECORD").innerHTML="aba";
}
