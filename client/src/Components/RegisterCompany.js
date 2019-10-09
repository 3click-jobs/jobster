import React, { Component } from 'react';

class RegisterCompany extends Component {
  render() {
    return (
        <div>
         <div className='jumbotron'>
          <div className="container register-form">
            <div className="form">
                <div className="note">
                    <p>Register Company</p>
                </div>
                <div className="form-content">
                    <div className="row">
                        <div className="col-md-12">
                            <div className="form-group">
                                <input type="text" className="form-control" placeholder="Company name *"/>
                            </div>
                            <div className="form-group">
                                <input type="number" className="form-control" placeholder="Company id No *"/>
                            </div>
                            <div className="form-group">
                                <input type="phone" className="form-control" placeholder="Phone"/>
                            </div>
                            <div className="form-group">
                                <input type="text" className="form-control" placeholder="Country *"/>
                            </div>     
                        
                            <div className="form-group">
                                <input type="email" className="form-control" placeholder="E-mail *"/>
                            </div>
                            <div className="form-group">
                                <input type="text" className="form-control" placeholder="Username *"/>
                            </div>
                            <div className="form-group">
                                <input type="password" className="form-control" placeholder="Password *"/>
                            </div>
                            <div className="form-group">
                                <input type="password" className="form-control" placeholder="Confirm Password *"/>
                            </div>                    
                   
                            <button type="button" className="btn btn-danger btn-lg">Login</button>
                     </div>
                </div>
            </div>
        </div>
        </div>
        </div> </div>
    );
  }
}
export default RegisterCompany;