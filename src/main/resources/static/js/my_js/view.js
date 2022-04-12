
function toggleReplyCommentForm(contentId) {
    console.log("content id =" + contentId);
    $(contentId).toggle();
}

function movePrevious(){
    // location.replace(document.referrer);
    // location.href="/board";
    history.back();
}
