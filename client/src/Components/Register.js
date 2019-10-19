import React, { Component } from 'react';
import '../App.css';
import { Link } from 'react-router-dom';



class Register extends Component {
  render() {
    return (
        
         <div className='jumbotron'>
          <div className="container">
           
            <div className='row '>
                
            <div className="col-md-6">
                        <div className="card" >
                        <img  className="card-img-top" alt="" />
                                 <div className="card-body">
                                 <h5 className="card-title note">EMPLOYEE</h5>
                              <p className="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
                         <Link to={'/registerperson'} className='btn btn-danger btn-lg'>Register Now</Link>
                         </div>
                         </div>                         
                           
                     </div>

                            
            <div className="col-md-6">
                        <div className="card" >
                        <img  className="card-img-top" alt=""/>
                                 <div className="card-body">
                                 <h5 className="card-title note">COMPANY</h5>
                              <p className="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
                         <Link to={'/registercompany'} className='btn btn-danger btn-lg'>Register Now</Link>
                         </div>
                         </div>                         
                           
                     </div>

            </div>

      </div>
  </div> 

    );
  }
}
export default Register;