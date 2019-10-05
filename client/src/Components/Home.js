import React, { Component } from 'react';
import '../App.css';

class Home extends Component {
  render() {
    return (
        <div >
           <div className="landing">
        <div className="home-wrap">
        <div className="home-inner">            
        </div>      
     </div>  
    </div>

    <div className="caption text-center">
        <h1>3clickjobs</h1>
        <h3>Find best job offers with 3clickjobs</h3>
        <button type="button" class="btn btn-primary btn-lg">Search Available Jobs</button>
        <button type="button" class="btn btn-primary btn-lg">Search Available Employees</button><br></br>
        <button type="button" class="btn btn-secondary btn-lg">Offer/Seek Job</button>
        </div>
        <div className="jumbotron"> 
            <div className="narrow text-center">
            <div className="col-12">
           <h3 className="heading">jobs</h3>
         <div className="heading-underline"></div>
    </div>
    </div>

        </div>
        </div>
    );
  }
}
export default Home;