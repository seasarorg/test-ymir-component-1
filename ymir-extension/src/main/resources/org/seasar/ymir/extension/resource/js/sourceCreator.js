$(function() {
<span tal:condition="sourceCreator/sourceCreatorSetting/inPlaceEditorEnabled" tal:omit-tag="">
    prepareForInPlaceEditor();
</span>
<span tal:condition="sourceCreator/sourceCreatorSetting/controlPanelEnabled" tal:omit-tag="">
    prepareForControlPanel();
</span>
});

function prepareForInPlaceEditor() {
    $('#__ymir__inPlaceEditor').editable({
        type: 'textarea',
        editBy: 'dblclick',
        submit: 'save',
        cancel: 'cancel',
        textareaFactory: function() {
            return $('<textarea />').attr('rows', 18).css({
                width: '90%'
            });
        },
        onLoadText: function() {
            var container = $('<textarea />');
            $this = this;
            $.ajax({
                async: false,
                data: {
                    path: location.href
                },
                dataType: 'text',
                type: 'POST',
                success: function(data) {
                    container.val(data);
                },
                url: '<span tal:replace="request/contextPath" />/__ymir__/editTemplate.do'
            });
            return container.val();
        },
        onSubmitting: function(param) {
            $.ajax({
                async: false,
                data: {
                    path: location.href,
                    body: param.current
                },
                type: 'POST',
                url: '<span tal:replace="request/contextPath" />/__ymir__/updateTemplate.do'
            });
            location.reload();
            return false;
        }
    });
}

function prepareForControlPanel() {
    var element = $('#__ymir__controlPanel');
    var container = $('<div />').append(element.children()).appendTo(element);
    element.data('inEffect', false);
    element.data('display', false);
    element.css('position', 'absolute');
    element.css({
        top: 0,
        left: ($(document).width() - element.width()) + 'px',
        width: element.width(),
        height: element.height()
    }).hover(function(event) {
        $this = $(this);
        if ($this.data('inEffect') == true) {
            return;
        }
        if ($this.data('display') == false) {
            $this.data('inEffect', true);
            container.show('slide', { direction: 'up' }, 800, function() {
                $this.data('display', true);
                $this.data('inEffect', false);
            });
        }
    }, function(event) {
        $this = $(this);
        if ($this.data('inEffect') == true) {
            return;
        }
        if ($this.data('display') == true) {
            $this.data('inEffect', true);
            container.hide('slide', { direction: 'up' }, 800, function() {
                $this.data('display', false);
                $this.data('inEffect', false);
            });
        }
    });
    container.hide();
}
