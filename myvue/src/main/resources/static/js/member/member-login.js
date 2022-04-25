let vu;
$().ready(() => {
    vu = new Vue({
        el: '#page',
        data: {
            member:{}
        },        
        created() {
            const me = this;
            this.fnLoad();
            let saveid = localStorage.getItem("saveid");
            if(saveid){
                me.member.userId = saveid;
            }
        },
        mounted() {
            const me = this;
            if(localStorage.getItem("saveid")){
                $('#saveid').prop('checked', true);
                $('.login-input.id>span').addClass('on');
            }
        },
        methods: {
            fnLoad() {
                if(URLSearch.get('expire')) {
                    alert("새로운 곳에서 로그인이 시도되었습니다.");
                    location.href = "/login";
                }
            },
            signIn() {
                const me = this;
                this.$validator.validateAll().then(success => {
                if(success) {
                    
                if ($('#saveid').prop('checked')) {
                    localStorage.setItem("saveid", me.member.userId);
                } else {
                    localStorage.removeItem('saveid');
                }
                
                let form = new FormData();
                form.append('userId', this.member.userId);
                form.append('userPwd', this.member.userPwd);

                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                axios.post(API_VERSION + '/login/security', form, {headers: {[header]: token}})
                .then(response => {
                    location.href = "/board";
                })
                .catch(error => {
                    alert(error.response.data.message);
                    if (error.response) {
                      // 요청이 이루어졌으며 서버가 2xx의 범위를 벗어나는 상태 코드로 응답했습니다.
                      console.log(error.response.data);
                      console.log(error.response.status);
                      console.log(error.response.headers);
                    }
                    console.log(error.config);
                });
                    } else {
                            alert("입력 정보가 맞지 않습니다. 아이디와 비밀번호를 다시 입력해주세요.");
                    }
                });
            }
        }
    });
});