import React, { Component } from 'react';
import '../App.css';
import { BrowserRouter as Router, Switch, Route, Link } from 'react-router-dom';


class Login extends Component {
  render() {
    return (
        <div>
         <div className='jumbotron'>
          <div className="container register-form">
            <div className="form">
                <div className="note">
                    <p>3clickjobs LogIn</p>
                </div>

                <div className="form-content">
                    <div className="row">
                      
                        <div className="col-md-12">
                            <div className="form-group">
                                <input type="text" className="form-control" placeholder="Username *" value=""/>
                            </div>
                            <div className="form-group">
                                <input type="text" className="form-control" placeholder="Password *" value=""/>
                            </div>                                                 
                            <button type="button" className="btn btn-danger btn-lg">Login</button>                            
                            <div className="form-group">
                            <Link to={'/register'}>You dont have account? Register now!</Link>
                            </div>
                     </div>
                </div>
            </div>
        </div>
      </div>
  </div> 
</div>
    );
  }
}
export default Login;