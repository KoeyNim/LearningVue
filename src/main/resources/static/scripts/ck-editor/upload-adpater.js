class UploadAdapter {
    constructor(loader, editor) {
        this.loader = loader;
        this.editor = editor;
    }
    upload() {
        let me = this;
        return me.loader.file.then(img => new Promise(((resolve, reject) => {
        const formData = new FormData();
        formData.append('img', img);
        $ajax.api({
            url: API_VERSION + '/image/temp',
            type: 'POST',
            data: formData,
            enctype : 'multipart/form-data',
            contentType : false,
            processData : false,
            cache : false,
        }).done((res) => {
            console.log('done', arguments);
            me.editor.execute('insertImage', { source: API_VERSION + '/image/find/'+ res.imgNm });
        }).fail((jqXHR, stat, err) => {
            console.log(stat+':'+err);
        });
        })))
    }
}
