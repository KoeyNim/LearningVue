class UploadAdapter {
    constructor(loader, editor) {
        this.loader = loader;
        this.editor = editor;
    }
    upload() {
        const me = this;
        return this.loader.file.then( file => new Promise(((resolve, reject) => {
        const formData = new FormData();
        formData.append('image', file);
        $.ajax({
            type : "POST",
            url : API_VERSION + '/imageupload',
            enctype : 'multipart/form-data',
            contentType : false,
            processData : false,
            cache : false,
            data : formData,
            beforeSend(xhr) {
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");
                xhr.setRequestHeader(header, token);
            },
        }).done(function(url) {
            console.log(url);
            me.editor.execute( 'insertImage', { source: url } );
        }).fail((jqXHR, stat, err) => {
            console.log(stat+':'+err);
        });
        })))
    }
}
