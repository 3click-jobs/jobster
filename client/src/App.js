import React, {Component} from 'react';
import { BrowserRouter as Router, Switch, Route, Link } from 'react-router-dom';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Navbar from './Components/Navbar';
import Home from './Components/Home';
import Login from './Components/Login';
import Contact from './Components/Contact';
import About from './Components/About';

class App extends Component {

  render() {
     return (
     <div>
        <Navbar/>
          <Switch>
              <Route exact path='/' component={Home} />
              <Route exact path='/login' component={Login} />
              <Route exact path='/contact' component={Contact} />
              <Route exact path='/about' component={About} />            
          </Switch>         
     </div>
    );
  }
}

export default App;
