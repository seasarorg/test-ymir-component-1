Event.observe(window, 'load', prepareForSourceCreator, false);

function prepareForSourceCreator() {
    prepareForInPlaceEditing();
    prepareForControlPanel();
}

function prepareForInPlaceEditing() {
    var bodyTags = $A(document.getElementsByTagName('body'));
    if (bodyTags.length == 0) {
        return;
    }

    var bodyTag = bodyTags.first();

    new SourceCreator.InPlaceEditor(
        bodyTag,
        '@CONTEXT_PATH@/__ymir__/updateTemplate.do',
        {
            cols: 80,
            rows: 25,
            highlightcolor: '#FFFFFF',
            clickToEditText: 'Double click to edit the template',
            loadTextURL: '@CONTEXT_PATH@/__ymir__/editTemplate.do?path='
                + encodeURIComponent(location.href),
            callback: function (form, value) {
                return 'path=' + encodeURIComponent(location.href)
                    + '&body=' + encodeURIComponent(value);
            },
            onComplete: function (transport, element) {
                window.location.reload();
            }
        }
    );
}

var SourceCreator = {};

SourceCreator.InPlaceEditor = Class.create();

Object.extend(SourceCreator.InPlaceEditor.prototype,
    Ajax.InPlaceEditor.prototype);
Object.extend(SourceCreator.InPlaceEditor.prototype, {
  registerListeners: function() {
    this._listeners = { };
    var listener;
    $H(this.Listeners).each(function(pair) {
      listener = this[pair.value].bind(this);
      this._listeners[pair.key] = listener;
      if (!this.options.externalControlOnly)
        this.element.observe(pair.key, listener);
      if (this.options.externalControl)
        this.options.externalControl.observe(pair.key, listener);
    }.bind(this));
  },
  Listeners: {
    dblclick: 'enterEditMode',
    keydown: 'checkForEscapeOrReturn',
    mouseover: 'enterHover',
    mouseout: 'leaveHover'
  }
});

function prepareForControlPanel() {
    new SourceCreator.ControlPanel();
}

SourceCreator.ControlPanel = Class.create();
Object.extend(SourceCreator.ControlPanel.prototype, {
    initialize: function() {
        this.element = $('__ymir__controlPanel');

        Element.setStyle(this.element, {
            'display': 'none',
            'position': 'absolute',
            'top': 0,
            'left': 0,
        });

        Event.observe(window, 'mousemove',
            this.update.bindAsEventListener(this), false);
    },
    update: function(event) {
        var mouseX = Event.pointerX(event);
        var mouseY = Event.pointerY(event);
        var clientWidth = document.body.clientWidth;
        if (mouseX > clientWidth / 2 && mouseY < 24) {
            Element.setStyle(this.element, {
                'left': (clientWidth - Element.getWidth(this.element)) + 'px',
            });
            Element.show(this.element);
        } else {
            Element.hide(this.element);
        }
    }
});
