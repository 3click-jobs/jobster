import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { ProductConsumer } from '../context';
import '../Style/Job.css';


class Job extends Component {
   
  render() {
    const {id, title, img, price, company, description} = this.props.job;
          
    return (   
    <section className="search-result-item">
      <a className="image-link" href="#"><img className="image" src={img}/></a>
      <div className="search-result-item-body">
          <div className="row">
              <div className="col-sm-9">
                    <h4 className="search-result-item-heading"><a href="#">{title}</a></h4>
                  <p className="info">{company}</p>
                  <p className="description">{description}</p>
              </div>
              <div className="col-sm-3 text-align-center">
                  <p className="value3 mt-sm">${price}</p>
                  <p className="fs-mini text-muted"><Link to={'/jobdetails'} id="jobdetails" className="btn btn-primary btn-info btn-sm">Details</Link></p>
                  <a className="btn btn-danger btn-info btn-sm" href="#">Apply Now</a>
              </div>
          </div>
      </div>
  </section>

    );
  }
}
export default Job;