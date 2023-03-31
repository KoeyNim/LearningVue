let vu;
$(() => {
    const saveid = localStorage.getItem('saveid');

    vu = new Vue({
        el: '#page',
        data: {
            member:{
                userId : !!saveid ? saveid : undefined
            }
        },        
        created() {
            const me = this;
            me.fnLoad();
        },
        mounted() {
            if(!!saveid) {
                $('#saveid').prop('checked', true);
                $('.login-input.id>span').addClass('on');
            }
        },
        methods: {
            fnLoad() {
                console.log('fnLoad', arguments);
                if(!!URLSearch.get('expire')) {
                    alert('새로운 곳에서 로그인이 시도되었습니다.');
                    location.href = '/member-login';
                }
            },
            fnLogin(e) {
                console.log('fnLogin', arguments);
                let me = this;
                me.$validator.validateAll().then((success) => {
                if(success) {
                    !!$('#saveid').prop('checked') ? localStorage.setItem('saveid', me.member.userId) : localStorage.removeItem('saveid');

                    let formData = new FormData();
                    var token = $("meta[name='_csrf']").attr("content");
                    var header = $("meta[name='_csrf_header']").attr("content");

                    formData.append('userId', me.member.userId);
                    formData.append('userPwd', me.member.userPwd);

                    axios.post(API_VERSION + '/member-login/security', formData, {headers: {[header]: token}})
                        .then((res) => {
                            location.href = '/board';
                        })
                        .catch((error) => {
                            alert(error.response.data.message);
                              // 요청이 이루어졌으며 서버가 2xx의 범위를 벗어나는 상태 코드로 응답했습니다.
                            console.log(error.response?.data);
                            console.log(error.response?.status);
                            console.log(error.response?.headers);
                            console.log(error.config);
                        });
                    } else {
                        alert('입력 정보가 맞지 않습니다. 아이디와 비밀번호를 확인해주세요.');
                    }
                });
            },
            fnSignUp(e) {
                console.log('fnSignUp', arguments);
                location.href = '/member-signup';
            }
        }
    });
});