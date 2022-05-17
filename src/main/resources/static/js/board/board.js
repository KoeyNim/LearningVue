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
          this.fnGetList();
        },
        methods: {
            goRegOrDetail(id = '') {
                location.href ='/board/' + (id ? 'detail?id=' + id : 'regist');
            },
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
                
                $.ajax({
                    type: 'GET',
                    url: API_VERSION + '/board' + '?pageIndex=' + me.pageIndex + 
                                                  '&pageSize='  + me.pageSize +
                                                  '&srchKey='   + me.srchKey + 
                                                  '&srchVal='   + me.srchVal,
                    beforeSend(xhr) {
                       var header = $("meta[name='_csrf_header']").attr("content");
                       var token = $("meta[name='_csrf']").attr("content");
                       xhr.setRequestHeader(header, token);
                    }
                }).done(function(response) {
                    me.result = response;
                    me.setPagination();
                }).fail(() => alert('잘못된 요청입니다.') );
            },
            downloadExcel() {
                location.href= API_VERSION + '/board/excel';
            },
            setPagination: function() {
                const me = this;
                let totalpages = me.result.totalPages;
                let currentPage = me.pageIndex+1;
                let pagingSize = me.pagingSize;
                let currentPagingBlockNumber = Math.ceil(currentPage/pagingSize);
                let startPageNumber = (currentPagingBlockNumber * pagingSize) - (pagingSize -1);
                let endPageNumber = (currentPagingBlockNumber * pagingSize) > totalpages ? totalpages : (currentPagingBlockNumber * pagingSize);
                me.pagingList = [];
                for (let i=startPageNumber; i<=endPageNumber; i++) {
                    me.pagingList.push(i);
                }
            }
        }
    });
});

// 사용하지 않는 에러 처리
/*$(document).ajaxError((event, response) => {
    console.log('common error', event, response);
    alert(response.responseJSON.message);
});*/