import React, { Component } from 'react';
import { Switch, Route } from 'react-router-dom';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Navbar from './Components/Navbar';
import Home from './Components/Home';
import Login from './Components/Login';
import Contact from './Components/Contact';
import About from './Components/About';
import RegisterPerson from './Components/RegisterPerson';
import RegisterCompany from './Components/RegisterCompany';
import Register from './Components/Register';
import Job from './Components/Job';
import JobList from './Components/JobList';
import JobDetails from './Components/JobDetails';

class App extends Component {

  render() {
     return (
     <React.Fragment>
        <Navbar/>
          <Switch>
              <Route exact path='/' component={Home} />
              <Route exact path='/login' component={Login} />
              <Route exact path='/contact' component={Contact} />
              <Route exact path='/about' component={About} />
              <Route exact path='/register' component={Register} />  
              <Route exact path='/registerperson' component={RegisterPerson} />       
              <Route exact path='/registercompany' component={RegisterCompany} />   
              <Route path='/job' component={Job} />   
              <Route path='/joblist' component={JobList} />      
              <Route path='/jobdetails' component={JobDetails} />    
          </Switch>         
     </React.Fragment>
    );
  }
}

export default App;
