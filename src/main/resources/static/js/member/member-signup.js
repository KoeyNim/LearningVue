let vu;
$(() => {
    vu = new Vue({
        el: '#content',
        data: {
            member: {},
            change: true
        },
        methods: {
            fnSignUp(e) {
                console.log('fnSignUp', arguments);
                let me = this;

                if(me.change) {
                    alert('ID 중복 확인은 필수입니다.');
                    return;
                }

                me.$validator.validateAll().then((success) => {
                    if(success) {
                        var token = $("meta[name='_csrf']").attr("content");
                        var header = $("meta[name='_csrf_header']").attr("content");

                        axios.post(API_VERSION + '/member/signup', me.member, {headers: {[header]: token}})
                        .then(res => {
                            alert(res.data.message);
                            location.href = '/';
                        })
                        .catch((error) => {
                            alert('잘못된 요청입니다.');
                            // 요청이 이루어졌으며 서버가 2xx의 범위를 벗어나는 상태 코드로 응답했습니다.
                            console.log(error.response?.data);
                            console.log(error.response?.status);
                            console.log(error.response?.headers);
                            console.log(error.config);
                        });
                    } else {
                        alert('입력 정보가 맞지 않습니다. 필수 항목을 다시 확인해주세요.');
                    }
                });
            },
            fnIdChk(e) {
                console.log('fnIdChk', arguments);
                let me = this;

                me.$validator.validate('userId').then((success) => {
                    if(success) {
                        $ajax.api({
                            url: API_VERSION + '/member/idchk',
                            data: {userId : me.member.userId},
                        }).done((res) => {
                            console.log('done', arguments);
                            alert(res.message);
                            me.change = false;
                        }).fail((res) => {
                            alert(res.responseJSON.message);
                        });
                    } else {
                        alert('입력 정보가 맞지 않습니다. 필수 항목을 다시 확인해주세요.');
                    }
                })
            }
        }
    });
});