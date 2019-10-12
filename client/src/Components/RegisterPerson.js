import React, { Component } from 'react';

class RegisterPerson extends Component {

    constructor(props){
        super(props);
        this.state = {

        }
    }

    handleRegisterUser =() => {
        fetch('http://localhost:8080/jobster/users/persons', {
            method: 'post',
            headers: {'Content-Type':'application/json'},
            body: {
             
            }
           });
    }



  render() {
    return (
        <div>
         <div className='jumbotron'>
          <div className="container register-form">
            <div className="form">
                <div className="note">
                    <p>Register Person</p>
                </div>
                <div className="form-content">
                    <div className="row">
                        <div className="col-md-12">
                            <div className="form-group">
                                <input type="text" className="form-control" placeholder="FirstName *"/>
                            </div>
                            <div className="form-group">
                                <input type="number" className="form-control" placeholder="LastName *"/>
                            </div>
                            <div className="form-group">
                                <input type="number" className="form-control" placeholder="Gender *"/>
                            </div>
                            <div className="form-group">
                                <input type="number" className="form-control" placeholder="Birth date*"/>
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
                   
                            <button type="button" className="btn btn-danger btn-lg" onClick={this.handleRegisterUser()}>Login</button>
                     </div>
                </div>
            </div>
        </div>
        </div>
        </div> </div>
    );
  }
}
export default RegisterPerson;