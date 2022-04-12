
function toggleReplyCommentForm(contentId) {
    // console.log("content id =" + contentId);
    $(contentId).toggle();
}

// 루트 댓글 만들기
function makeRootComment(){
    const commentForm = document.getElementById("commentForm");
    const address = commentForm.action;
    const boardNo = address.substring(address.indexOf("board/") + 6, address.indexOf("/comment"));
    const writer = commentForm.writer.value;
    const content = commentForm.content.value;

    if(content.trim() == ""){
        alert("내용을 입력해주세요.");
        return;
    }

    $.ajax({
        url: address,
        type: "POST",
        beforeSend: function (xhr) {
            const token = $("meta[name='_csrf']").attr("content");
            const header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },
        data: {
            "boardNo": boardNo,
            "writer": writer,
            "content": content
        },
        success: function (data) {
            if(data.success ='success'){
                $("#replies-root").load(window.location.href + " #replies-root");
            }
        },
        error: function (data) {
            alert("error: 댓글등록에 실패했습니다.");
        }
    });
}


// 답글 만들기
function makeSubComment(form){

    if(form.content.value.trim() == ""){
        alert("내용을 입력해주세요.");
        return;
    }

    let replyForm = $(form).serialize();
    $.ajax({
        url: "/board/commentReply",
        type: "POST",
        beforeSend: function (xhr) {
            const token = $("meta[name='_csrf']").attr("content");
            const header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },
        data: replyForm,

        success: function (data) {
            if(data.success ='success'){
                $('#replies-root').load(window.location.href + " #replies-root");
            }
        },
        error: function (data) {
            alert("error: 답글등록에 실패했습니다.");
        }
    });
}