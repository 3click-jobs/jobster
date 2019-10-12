import React, {Component} from 'react';
import { Link } from 'react-router-dom';

class Navbar extends Component {
  constructor(props){
    super(props);
      this.state = {
        isTop: true       
      };
  }
  

  componentDidMount() {
    document.addEventListener('scroll', () => {
      const isTop = window.scrollY < 100;
      if (isTop !== this.state.isTop) {
          this.setState({ isTop })
      }
    });
  }

  render() {

      return (  
        <nav inverse toggleable style={{ top: 0 }}
        className={this.state.isTop ? 'navbar navbar-expand-md fixed-top top-nav' : 'navbar navbar-expand-md fixed-top top-nav light-header'} >
       
      
	<div className="container">
		  <a className="navbar-brand" href="#"><strong>3CLICKJOBS</strong></a>
		  <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
		    <span className="navbar-toggler-icon"><i className="fa fa-bars" aria-hidden="true"></i></span>
		  </button>

		  <div className="collapse navbar-collapse" id="navbarSupportedContent">
		    <ul className="navbar-nav ml-auto">
		     
          <li className='nav-item active'><Link to={'/'} className="nav-link"> Home </Link></li>
		      
		      <li className="nav-item">
		        <a className="nav-link" href="#">About</a>
		      </li>
		      <li className="nav-item">
		        <a className="nav-link" href="#">Contact</a>
		      </li>
          <li className="nav-item">
          <li className='nav-item'><Link to={'/login'} className="nav-link"> LogIn </Link></li>
		      </li>
          <li className="nav-item">
		        <a className="nav-link" href="#">Regiter</a>
		      </li>
		    </ul>
		  </div>	
	</div>
</nav>  


          
    
    
    );
  }
}

export default Navbar;