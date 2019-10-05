import React, {Component} from 'react';
import { BrowserRouter as Router, Switch, Route, Link } from 'react-router-dom';
import Home from './Home';
import Login from './Login';
import Contact from './Contact';
import About from './About';

class Navbar extends Component {

  render() {
     return (
        <div> 
        
        <div>
          <nav className="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
            <a className="navbar-brand" href="#">3CLICKJOBS</a>
            <ul className="navbar-nav ml-auto">
                <li className='nav-item ml-5'><Link to={'/'} className="nav-link"> Home </Link></li>
                <li><Link to={'/contact'} className="nav-link"> Contact </Link></li>
                <li><Link to={'/about'} className="nav-link"> About </Link></li>
                <li><Link to={'/login'} className="btn btn-danger" role="button"> LogIn </Link></li>
            </ul>
          </nav>
        </div>       
    </div>
    );
  }
}

export default Navbar;