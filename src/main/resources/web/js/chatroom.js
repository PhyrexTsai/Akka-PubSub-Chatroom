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
        $("#chat-room-name").text($.urlParam("topic"));
        console.log(ws);
        return ws;
    }

    var websocket = new WebSocket(webSocket());

    var sendMessage = function() {
        if($("#send").val().trim() !== ""){
            websocket.send($("#send").val());
            $("#send").val("");
        }
    }

    var receiveEvent = function(event) {
        var data = JSON.parse(event.data);
        var chat = $(".chat-space");
        var group = $("<div></div>").addClass("group-rom");
        if(data.message != ""){
            if(data.type === "ChatMessage"){
                var sender = $("<div></div>").addClass("first-part odd").html(data.sender);
                var message = $("<div></div>").addClass("second-part").html(data.message);
                var time = $("<div></div>").addClass("third-part").html("");
                group.append(sender).append(message).append(time);
            }else{
                var info = $("<div></div>").addClass("info-part odd").html(data.message);
                group.append(info);
            }
            $(".chat-space").append(group);
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
    $(window).unload(function() {
        websocket.close();
    });

    websocket.onmessage = receiveEvent;
});