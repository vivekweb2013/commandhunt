import React, { Component } from "react";

class PermissionInput extends Component {

    handleChange(e, assigneeIndex, permissionIndex) {
        const binaryPermissions = [4, 2, 1];
        let value = this.props.value[0];
        const valArray = value.split("").map(v => +v);
        valArray[assigneeIndex] = valArray[assigneeIndex] + (e.target.checked ? 1 : -1) * binaryPermissions[permissionIndex];
        this.props.handleChange(e.target.name, [valArray.join("")]);
    }

    handleTextChange(e) {
        e.stopPropagation();
        const value = e.target.value;
        if (e.target.value === "" || e.target.value.match(/^([0-9]{3})$/g)) {
            this.props.handleChange(e.target.name, [value]);
        }
    }

    toggle(e) {
        e.stopPropagation();
        const { name, value } = this.props;
        const valArray = value[0].split("").map(v => +v);
        valArray.length === 0 ? this.props.handleChange(name, ["000"]) : this.props.handleChange(name, [""])
    }

    render() {
        const { id, name, pattern, required, disabled, value } = this.props;
        const binaryPermissions = [4, 2, 1];
        const valArray = value[0].split("").map(v => +v); // get array of permission numbers

        return (<div className="permission-ip-wrapper">
            <span className="dynamic-text-ip-wrapper">
                <input id={id} name={name} pattern={pattern} type="text" value={value[0]}
                    onChange={this.handleTextChange.bind(this)} required={required} disabled={disabled} />
                {!disabled && <input type="button" disabled={disabled} onClick={e => this.toggle(e)} className="small" value={valArray.length === 0 ? " ＋ " : " － "} />}
            </span>
            <div className="permission-container">
                {valArray.map((v, i) => <div key={i} className="permission-row">
                    <span className="assignee">{i === 0 ? "Owner" : (i === 1 ? "Group" : "Others")}</span>
                    <span className="permissions">
                        {["Read", "Write", "Execute"].map((p, j) =>
                            <span key={`${id}_${i}_${j}`}><span className="permission-type">{p.charAt(0)}<span className="lg">{p.substring(1)}</span></span>
                                <input id={`${id}_${i}_${j}`} type="checkbox" name={name}
                                    onChange={(e) => this.handleChange(e, i, j)}
                                    disabled={disabled} checked={(v | binaryPermissions[j]) === v} />
                                <label htmlFor={`${id}_${i}_${j}`}>
                                    <svg viewBox="0,0,50,50"><path d="M5 30 L 20 45 L 45 5"></path></svg>
                                </label>
                            </span>)}
                    </span>
                </div>)}
            </div>
        </div>);
    }

}

export default PermissionInput;