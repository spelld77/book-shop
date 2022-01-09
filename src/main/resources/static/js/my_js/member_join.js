// 아이디 중복 체크
function duplicateCheck(){

    let idField = document.getElementById('mId');
    let modalIdField = document.getElementById('input_id');


    $.ajax({
        url: "/checkUniqueId",
        data: { inputId: modalIdField.value },
        type: "GET",
        dataType: "text",

        success: function(result){
            console.log(result);
            if(result == 'unique'){
                alert("사용가능한 아이디입니다.")
                $('#idcheckModal').modal('hide');
                idField.value = modalIdField.value;
            } else{
                alert("사용할수 없는 아이디입니다.")
            }
        },
        error: function (error){
            console.log('error');
        }
    });

}
