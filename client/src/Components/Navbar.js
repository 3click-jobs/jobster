import React, {Component} from 'react';
import { Link } from 'react-router-dom';

class Navbar extends Component {
  constructor(props){
    super(props);
      this.state = {
        isTop: true,
        navBarColor :""
      };
  }

  handleNavBarColor = (e) => {
    console.log(e.target.id);
    switch(e.target.id){
      case "home":
        this.setState({navBarColor : ""});
        return
      case "about":
        this.setState({navBarColor : "blue"});
        return;
      case "contact":
        this.setState({navBarColor : "red"});
        return;
      case "login":
        this.setState({navBarColor : "green"});
        return;
      case "register":
        this.setState({navBarColor : "yellow"});
        return;
      case "job":
          this.setState({navBarColor : 'gray'});
          return;
      case "joblist":
          this.setState({navBarColor : 'gray'});
          return;
      default :;
    }
  }
  

  componentDidMount() {
    document.addEventListener('scroll', () => {
      const isTop = window.scrollY < 100;
      if (isTop !== this.state.isTop) {
          this.setState({ isTop });
      }
    });
  }

  render() {
      return (  
        <nav style={{ top: 0 , backgroundColor:this.state.navBarColor}} 
            className={this.state.isTop ? 'navbar navbar-expand-md fixed-top top-nav' : 
            'navbar navbar-expand-md fixed-top top-nav light-header'} >
            <div className="container">
                <a className="navbar-brand" href="/"><strong>3CLICKJOBS</strong></a>
                  <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"><i className="fa fa-bars" aria-hidden="true"></i></span>
                  </button>
              <div className="collapse navbar-collapse" id="navbarSupportedContent">
                <ul className="navbar-nav ml-auto">		     
                  <li className='nav-item active'><Link to={'/'} id="home" className="nav-link" onClick={(e) => this.handleNavBarColor(e)}> Home </Link></li>     
                  <li className="nav-item" ><Link to={'/about'} id="about" className="nav-link" onClick={(e) => this.handleNavBarColor(e)} >About</Link> </li>
                  <li className="nav-item"><Link to={'/contact'} id="contact" className="nav-link" onClick={(e) => this.handleNavBarColor(e)} >Contact</Link> </li>
                  <li className='nav-item'><Link to={'/login'} id="login" className="nav-link" onClick={(e) => this.handleNavBarColor(e)} > LogIn </Link></li>
                  <li className='nav-item'><Link to={'/register'} id="register" className="nav-link" onClick={(e) => this.handleNavBarColor(e)} > Register </Link></li>
              </ul>
            </div>	
	      </div>
      </nav>    
    );
  }
}

export default Navbar;