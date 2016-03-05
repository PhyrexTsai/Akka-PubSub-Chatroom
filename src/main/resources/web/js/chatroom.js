$(document).ready(function(){

    var urlParam = function(name){
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        if (results==null){
           return null;
        }
        else{
           return results[1] || 0;
        }
    }

    var webSocket = function(){
        var port = "";
        var protocol = "ws://";
        if(window.location.port !== ""){
            port = ":" + window.location.port;
        }
        if(window.location.protocol === "https:"){
            protocol = "wss://"
        }
        var ws = protocol + window.location.hostname + port;
        ws += "/chat?name=";
        ws += urlParam("name");
        ws += "&topic=";
        ws += urlParam("topic");
        $("#chat-room-name").text(urlParam("topic"));
        console.log(ws);
        return ws;
    }

    var websocket = new WebSocket(webSocket());

    var sendMessage = function() {
        if($("#send").val().trim() !== ""){
            var date = new Date();
            var message = new Object();
            message['messageType'] = $("#send").attr("messagetype");
            message['topic'] = urlParam("topic");
            message['sender'] = urlParam("name");
            message['time'] = date.getTime();
            message['message'] = $("#send").val();
            //console.log("Message : " + JSON.stringify(message));

            websocket.send(JSON.stringify(message));
            $("#send").val("");
        }
    }

    var receiveEvent = function(event) {
        //console.log("##" + event.data);
        if(event.data === "") return;
        var data = JSON.parse(event.data);
        var chat = $(".chat-space");
        var group = $("<div></div>").addClass("group-rom");
        if(data.message != ""){
            if(data.messageType === "ChatMessage"){
                var sender = $("<div></div>").addClass("first-part odd").html(data.sender);
                var message = $("<div></div>").addClass("second-part").html(data.message);
                var time = $("<div></div>").addClass("third-part").text("");
                group.append(sender).append(message).append(time);
            }else if(data.messageType === "SnapMessage"){
                var snap = $("<div></div>").addClass("snap-part").text("限時訊息");
                var sender = $("<div></div>").addClass("first-part odd").html(data.sender);
                var message = $("<div></div>").addClass("second-part").append(snap);
                snap.bind("click", {"msg" : data.message, "node" : group}, function(){
                    $(this).text(data.message);
                    setTimeout(function(){group.remove();}, 10000);
                });
                var time = $("<div></div>").addClass("third-part").text("");
                group.append(sender).append(message).append(time);
            }else{
                var info = $("<div></div>").addClass("info-part odd").html(data.message);
                group.append(info);
            }
            $(".chat-space").append(group);
            $.each($(".group-rom"), function(){
                console.log("height : " + $(this).height());
            });
            var height = $(".first-part").size() * 48;
            $(".chat-space").animate({ scrollTop: height }, "slow");
        }
        if(data.member != undefined){
            memberList(data.member);
        }
    }

    var handleReturnKey = function(e) {
        if(e.charCode == 13 || e.keyCode == 13) {
            e.preventDefault();
            sendMessage();
        }
    }

    var memberList = function(members) {
        $(".chat-member").empty();
        $.each(members, function(index, value){
            var group = $("<div></div>").addClass("group-rom");
            var member = $("<div></div>").addClass("member-part");
            var icon = $("<span></span>").addClass("glyphicon glyphicon-user");
            member.append(icon);
            member.append(" " + value);
            group.append(member);
            $(".chat-member").append(group);
        });
    }

    var pokeServer = function() {
        websocket.send("");
    }

    setInterval(pokeServer, 30000);

    $("#send").keypress(handleReturnKey);
    $("#sendButton").click(function(){
        sendMessage();
    });
    $("#chat-leave").click(function(){
        websocket.close();
        window.location = "index.html";
    });
    $("#chatButton").click(function(){
        $("#chatButton").addClass("active");
        $("#snapButton").removeClass("active");
        $("#send").attr("messagetype", $(this).attr("messagetype"));
        $("#send").attr("placeholder", "say something");
        $("#send").focus();
    });
    $("#snapButton").click(function(){
        $("#snapButton").addClass("active");
        $("#chatButton").removeClass("active");
        $("#send").attr("messagetype", $(this).attr("messagetype"));
        $("#send").attr("placeholder", "limit message");
        $("#send").focus();
    });
    $(window).unload(function() {
        websocket.close();
    });

    websocket.onmessage = receiveEvent;
});