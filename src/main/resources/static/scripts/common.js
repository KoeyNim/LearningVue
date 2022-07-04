function ajaxAPI(type, url, data, options) {

    let ajaxParam = {
        type: type,
        url: url,
    };

    if (data) {
        Object.assign(ajaxParam, {data: data});
    };

    if (options) {
        Object.assign(ajaxParam, options);
    };
    
    // GET 방식에서는 csrf를 적용할 필요가 없음.   
    if (type !== 'GET') {
        let csrf = {
            beforeSend(xhr) {
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");
                xhr.setRequestHeader(header, token);
            }};
        Object.assign(ajaxParam, csrf);
    };
    console.log("type : ", type, ", ", "url : ", url, ", ", "data : ", data);

    return $.ajax(ajaxParam);
}

function setPagination(totalPage, currentPage, pageSize) {
    let currentPagingBlockNumber = Math.ceil(currentPage/pageSize);
    let blockPageNumber = (currentPagingBlockNumber * pageSize);
    let startPageNumber = blockPageNumber - (pageSize -1);
    let endPageNumber = blockPageNumber > totalPage ? totalPage : blockPageNumber;
    let pagingList = [];
    
    for (let i=startPageNumber; i<=endPageNumber; i++) {
        pagingList.push(i);
    }
    return pagingList;
}