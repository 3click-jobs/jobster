import React, { Component } from 'react';

class RegisterCompany extends Component {

    constructor(props){
        super(props);
        this.state = {

        }
    }

    handleRegisterCompany = () => {
        fetch('http://localhost:8080/jobster/users/companies', {
            method: 'post',
            mode: 'no-cors',
            headers: {'Content-Type':'application/json'},
            body: {
                "companyName": "tester",
                "companyId": 1014090891254,
                "mobilePhone": "+38169010015",
                "email": "saqo0s0s@gmail.com",
                "city": "Novi Sad",
                "country" : "Serbia",
                "accessRole": "ROLE_USER",
                "iso2Code": "SER",
                "countryRegion": "Vojvodina",
                "longitude" : 38.0,
                "latitude": 39.1,
                "detailsLink" : "Aaaaaaa",
                "username": "Test123456789012",
                "password" : "Test12345678901",
                "confirmedPassword": "Test12345678901"
    }
           }).then(response => console.log(response));
    }

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
                   
                            <button type="button" onClick={this.handleRegisterCompany()} className="btn btn-danger btn-lg">Login</button>
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