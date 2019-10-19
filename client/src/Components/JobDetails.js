import React, { Component } from 'react';
import { JobConsumer } from '../context';
import { Link } from 'react-router-dom';


class JobDetails extends Component {
   
  render() {
    return (
        <JobConsumer>
            {value => {
              const {id, title, img, price, company, description} = value.jobDetails;
              console.log(company);
              return (
                <div><h1>{company}</h1></div>
              );
            }}        
        </JobConsumer>  
    );
  }
}
export default JobDetails;