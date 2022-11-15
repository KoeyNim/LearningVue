let vu;
$(() => {
    vu = new Vue({
        el: '#contents',
        data: {
            srch: {
              page:0,
              size:5,
              sort:'id,desc',
              srchKey: undefined,
              srchVal: undefined,
            },
            page: {
              pageCount: 5,
              totalPages: 0,
              start: 0,
              numbers:[],
            },
            list:{},
            options: pageSizeArr,
            srchOptions: [
                {text: '전체', value: undefined},
                {text: '제목', value: 'title'},
                {text: '작성자', value: 'userId'},
            ],
        },
        created() {
          let me = this;
          me.fnGets();
//          // sessionStorage 체크 후 data에 반영
//          const pageOptions = JSON.parse(sessionStorage.getItem('pageOptions'));
//          if(!!pageOptions) {
//              Object.assign(this._data, pageOptions);
//          }
//          this.fnGetList(pageOptions ? pageOptions.pageIndex ? pageOptions.pageIndex : null : null);
        },
        methods: {
            fnGets(e) {
                console.log('fnGets', arguments);
                let me = this;
//                const pageOptions = {
//                    pageIndex : me.pageIndex,
//                    pageSize : me.pageSize,
//                    srchKey : me.srchKey,
//                    srchVal : me.srchVal
//                };
//                sessionStorage.setItem('pageOptions', JSON.stringify(pageOptions));
                ajaxAPI('GET', API_VERSION + '/board', me.srch
                ).done((res) => {
                    console.log('done', res, arguments)
                    Object.assign(me.page, res);
                    me.list = res.content;
                    me.page.numbers = setPagination(me.page.totalPages , me.page.number+1, me.page.pageCount);
                }).fail(() => alert('잘못된 요청입니다.'));
            },
            fnPage(e, page) {
              console.log('fnPage', arguments);
              let me = this;
              if (!page) {
                  me.srch.page = 0;
              } else {
                  me.srch.page = page;
              }
              
              if (!!page && page < 0) { // 요청 페이지 번호가 존재하며 최소값 보다 낮을 경우 0으로 초기화
                  me.srch.page = 0;
              }
              
              if (me.page.totalPages > 0 && me.srch.page >= me.page.totalPages) { // 요청 페이지 번호가 total값 보다 높을 경우 total값으로 초기화
                  me.srch.page = me.page.totalPages -1;
              }
              me.fnGets(e);
            },
            goRegist() {
                location.href ='/board/regist';
            },
            goDetail(id) {
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