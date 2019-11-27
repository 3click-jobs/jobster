import React, { Component } from 'react';


class Filter extends Component {
   
  render() {
    return (       
        <div className="card">
        <div className="card-group-item">
            <header className="card-header">
                <h6 className="title">Job Category</h6>
            </header>
            <div className="filter-content">
                <div className="card-body">
                <form>
                    <label className="form-check">
                      <input className="form-check-input" type="checkbox" value=""/>
                      <span className="form-check-label">
                        All Category
                      </span>
                    </label> 
                    <label className="form-check">
                      <input className="form-check-input" type="checkbox" value=""/>
                      <span className="form-check-label">
                        IT Programming
                      </span>
                    </label>  
                    <label className="form-check">
                      <input className="form-check-input" type="checkbox" value=""/>
                      <span className="form-check-label">
                        Marketing
                      </span>
                    </label>  
                </form>                
                </div> 
            </div>
        </div> 
        
        <div className="card-group-item">
            <header className="card-header">
                <h6 className="title">Job Duration </h6>
            </header>
            <div className="filter-content">
                <div className="card-body">
                <label className="form-check">
                  <input className="form-check-input" type="radio" name="exampleRadio" value=""/>
                  <span className="form-check-label">
                    Per Hour
                  </span>
                </label>
                <label className="form-check">
                  <input className="form-check-input" type="radio" name="exampleRadio" value=""/>
                  <span className="form-check-label">
                    Part-time
                  </span>
                </label>
                <label className="form-check">
                  <input className="form-check-input" type="radio" name="exampleRadio" value=""/>
                  <span className="form-check-label">
                    Some other option
                  </span>
                </label>
                </div> 
            </div>
        </div> 
        <div className="card-group-item">
            <header className="card-header">
                <h6 className="title">Price Range</h6>
            </header>
            <div className="filter-content">
                <div className="card-body">
                <div className="form-row">
                <div className="form-group col-md-6">
                  <label>Min</label>
                  <input type="number" className="form-control" id="inputEmail4" placeholder="$0"/>
                </div>
                <div className="form-group col-md-6 text-right">
                  <label>Max</label>
                  <input type="number" className="form-control" placeholder="$1,000"/>
                </div>
                </div>
                </div> 
            </div>
        </div>        
    </div> 
    );
  }
}
export default Filter;