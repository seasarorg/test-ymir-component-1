$(function() {
    prepareForSourceCreator();
});

function prepareForSourceCreator() {
    prepareForInPlaceEditing();
    prepareForControlPanel();
}

function prepareForInPlaceEditing() {
    $('#__ymir__inPlaceEditor');
    if (!inPlaceEditorTag) {
        return;
    }

    new SourceCreator.InPlaceEditor(
        inPlaceEditorTag,
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
    var controlPanel = $('__ymir__controlPanel');
    if (!controlPanel) {
        return;
    }

    new SourceCreator.ControlPanel(controlPanel);
}

SourceCreator.ControlPanel = Class.create();
Object.extend(SourceCreator.ControlPanel.prototype, {
    initialize: function(element) {
        this.element = element;
        this.visible = true;
        this.inEffect = false;

        var clientWidth = document.body.clientWidth;
        Element.setStyle(this.element, {
            'display': 'none',
            'position': 'absolute',
            'top': 0,
            'left': (clientWidth - Element.getWidth(this.element)) + 'px'
        });
        this.hide();

        var bodyTags = $A(document.getElementsByTagName('body'));
        if (bodyTags.length == 0) {
            return;
        }
        Event.observe(bodyTags.first(), 'mousemove',
            this.update.bindAsEventListener(this), false);
    },
    update: function(event) {
        var mouseX = Event.pointerX(event);
        var mouseY = Event.pointerY(event);
        var clientWidth = document.body.clientWidth;
        if (mouseX > clientWidth / 2 && mouseY < 24) {
            this.show();
        } else {
            this.hide();
        }
    },
    show: function() {
        if (!this.visible && !this.inEffect) {
            this.inEffect = true;
            var clientWidth = document.body.clientWidth;
            Element.setStyle(this.element, {
                'left': (clientWidth - Element.getWidth(this.element)) + 'px'
            });
            Element.show(this.element);
            new Effect.Opacity(this.element, {
                from: 0.0,
                to: 1.0,
                duration: 1.0,
                afterFinish: function() {
                    this.visible = true;
                    this.inEffect = false;
                }.bind(this)
            });
        }
    },
    hide: function() {
        if (this.visible && !this.inEffect) {
            this.inEffect = true;
            new Effect.Opacity(this.element, {
                from: 1.0,
                to: 0.0,
                duration: 1.0,
                afterFinish: function() {
                    Element.hide(this.element);
                    this.visible = false;
                    this.inEffect = false;
                }.bind(this)
            });
        }
    }
});
