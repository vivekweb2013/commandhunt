import React, { Component } from "react";
import "./ItemsPerPage.scss";

class ItemsPerPage extends Component {
    render() {
        const pageSizeOptions = [5, 10, 25];

        if (pageSizeOptions.indexOf(this.props.pageSize) === -1) {
            //In case custom pageSize mentioned in url query param
            pageSizeOptions.push(this.props.pageSize);
        }

        return (
            <div className="items-per-page">
                <span className="icon-pagesize" title="Items per page"></span>
                <select defaultValue={this.props.pageSize} onChange={(e) => this.props.handlePageSizeChange(e)}>
                    {pageSizeOptions.sort((a, b) => a - b).map(pageSize => <option key={pageSize} value={pageSize}>{pageSize}</option>)}
                </select>
            </div>
        )
    }
}

export default ItemsPerPage;