let vu;
$(document).ready(function() {
    vu = new Vue({
        el: '#page',
        data: {
            member:{}
        },
        methods: {
            signUp() {
                this.$validator.validateAll().then(success => {
                    if(success) {
                        var token = $("meta[name='_csrf']").attr("content");
                        var header = $("meta[name='_csrf_header']").attr("content");
                        axios.post(API_VERSION + '/member/signup', this.member, {headers: {[header]: token}})
                        .then(response => {
                            alert(response.data.message);
                            location.href = '/';
                        })
                        .catch(error => {
                            alert('잘못된 요청입니다.');
                            if (error.response) {
                              // 요청이 이루어졌으며 서버가 2xx의 범위를 벗어나는 상태 코드로 응답했습니다.
                              console.log(error.response.data);
                              console.log(error.response.status);
                              console.log(error.response.headers);
                            }
                            console.log(error.config);
                        });
                    } else {
                        alert("입력 정보가 맞지 않습니다. 필수 항목을 다시 확인해주세요.");
                    }
                });
            }
        }
    });
});