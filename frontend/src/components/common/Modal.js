import React, { Component } from 'react';
import { createPortal } from "react-dom";
import './Modal.scss';


class Modal extends Component {
    constructor(props) {
        super(props);
        // Create a div that we'll render the modal into. Because each
        // Modal component has its own element, we can render multiple
        // modal components into the modal container.
        this.el = document.createElement('div');
        this.el.setAttribute('class', 'modal-wrapper');
    }

    componentDidMount() {
        // Append the element into the DOM on mount. We'll render
        // into the modal container element (see the HTML tab).
        document.getElementById('modal-root').appendChild(this.el);
    }

    componentWillUnmount() {
        // Remove the element from the DOM when we unmount
        document.getElementById('modal-root').removeChild(this.el);
    }

    /**
     * These "type" values are supported - info, warn, error, confirm
     * If "type" prop is not specified, info will be default value
     */
    render() {
        const { title, children, onClose, onConfirm } = this.props;
        const type = this.props.type || 'info';
        // Use a portal to render the children into the element
        return createPortal(
            // Any valid React child: JSX, strings, arrays, etc.
            <div class="modal">
                <div class="modal-content">
                    <span class="top-close-btn" onClick={e => onClose()}>&#x2715;</span>
                    <div class={`modal-header ${type}`}>
                        {title}
                    </div>
                    <div class="modal-body">
                        {children}
                    </div>
                    <div class="modal-footer">
                        {type === 'confirm' && <button class="footer-btn confirm" onClick={e => onConfirm()}>OKAY</button>}
                        <button class="footer-btn" onClick={e => onClose()}>{type === 'confirm' ? 'CANCEL' : 'CLOSE'}</button>
                    </div>
                </div>
            </div>,
            // A DOM element
            this.el
        );
    }
}

export default Modal;
