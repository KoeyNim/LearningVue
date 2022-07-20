let vu;
$(document).ready(function() {
    vu = new Vue({
        el: '#page',
        data: {
            pagingSize:5,
            pageIndex:0,
            pageSize:5,
            pagingList:[],
            options: pageSizeArr,
            result:{},
            srchOptions: [
                {text: '전체', value: ''},
                {text: '제목', value: 'title'},
                {text: '작성자', value: 'userId'},
            ],
            srchKey:'',
            srchVal:''

        },
        created() {
          if(!!sessionStorage.getItem('pageOptions')) {
              const pageOptions = JSON.parse(sessionStorage.getItem('pageOptions'));
              Object.assign(this._data, pageOptions);
          }
          this.fnGetList();
          sessionStorage.removeItem('pageOptions');
        },
        methods: {
            fnGetList(idx) {
                const me = this;
                if (!idx) {
                    me.pageIndex = 0;
                } else {
                    me.pageIndex = idx;
                }
                
                if (!!idx && idx < 0) { // 요청 페이지 번호가 존재하며 최소값 보다 낮을 경우 0으로 초기화
                    me.pageIndex = 0;
                }
                
                if (me.result.totalPages > 0 && me.pageIndex >= me.result.totalPages) { // 요청 페이지 번호가 total값 보다 높을 경우 total값으로 초기화
                    me.pageIndex = me.result.totalPages -1;
                }
                
                const url = API_VERSION + '/board' + '?pageIndex=' + me.pageIndex + 
                                               '&pageSize='  + me.pageSize +
                                               '&srchKey='   + me.srchKey + 
                                              '&srchVal='   + me.srchVal;
                ajaxAPI('GET', url
                ).done((response) => {
                    me.result = response;
                    me.pagingList = setPagination(me.result.totalPages, me.pageIndex+1, me.pagingSize)
                }).fail(() => alert('잘못된 요청입니다.'));
            },
            goRegist() {
                location.href ='/board/regist';
            },
            goDetail(id) {
                const me = this;
                const pageOptions = {
                    pageIndex : me.pageIndex,
                    pageSize : me.pageSize,
                    srchKey : me.srchKey,
                    srchVal : me.srchVal
                };
                sessionStorage.setItem('pageOptions', JSON.stringify(pageOptions));
                location.href ='/board/detail?id=' + id;
            },
            downloadExcel() {
                location.href= API_VERSION + '/board/excel';
            },
        }
    });
});

// 사용하지 않는 에러 처리
/*$(document).ajaxError((event, response) => {
    console.log('common error', event, response);
    alert(response.responseJSON.message);
});*/