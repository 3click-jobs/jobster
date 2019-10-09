import React, {Component} from 'react';
import { Link } from 'react-router-dom';

class Navbar extends Component {

  render() {
     return (
        
        
          // <nav classNameName="navbar navbar-expand-md navbar-light bg-light fixed-top">
          //   <a classNameName="navbar-brand" href="#">3CLICKJOBS</a>
          //   <ul classNameName="navbar-nav ml-auto">
          //       <li classNameName='nav-item active'><Link to={'/'} classNameName="nav-link"> Home </Link></li>
          //       <li classNameName='nav-item'><Link to={'/contact'} classNameName="nav-link"> Contact </Link></li>
          //       <li classNameName='nav-item'><Link to={'/about'} classNameName="nav-link"> About </Link></li>
          //       <li classNameName='nav-item'><Link to={'/login'} classNameName="btn btn-danger" role="button"> LogIn </Link></li>
          //   </ul>
          // </nav>

        <nav className="navbar navbar-expand-lg navbar-light bg-light">
          <a className="navbar-brand" href="#">3CLICKJOBS</a>
          <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span classNameName="navbar-toggler-icon"></span>
          </button>
        
          <div className="collapse navbar-collapse" id="navbarSupportedContent">
            <ul className="navbar-nav mr-auto">
            <li classNameName='nav-item active'><Link to={'/'} classNameName="nav-link"> Home </Link></li>
            <li classNameName='nav-item'><Link to={'/about'} classNameName="nav-link"> About </Link></li>
              
              <li classNameName='nav-item'><Link to={'/contact'} classNameName="nav-link"> Contact </Link></li>
            </ul>
            <form className="form-inline my-2 my-lg-0">
              <a className="nav-link" href="#">Register</a>
              <li classNameName='nav-item'><Link to={'/login'} classNameName="nav-link"> LogIn </Link></li>
              <button className="btn btn-primary">POST JOB</button>
            </form>
          </div>
        </nav>


          
    
    
    );
  }
}

export default Navbar;