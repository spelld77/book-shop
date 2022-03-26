
function toggleReplyCommentForm(contentId) {
    console.log("content id =" + contentId);
    $(contentId).toggle();
}

function movePrevious(){
    // history.go(-1);
    location.replace(document.referrer);
}
