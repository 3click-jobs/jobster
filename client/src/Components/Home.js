import React, { Component } from 'react';
import '../App.css';
import Navbar from './Navbar';

class Home extends Component {
  render() {
    return (
    //     <div >
    //        <div classNameNameNameName="landing">
    //     <div classNameNameNameName="home-wrap">
    //     <div classNameNameNameName="home-inner">            
    //     </div>      
    //  </div>  
    // </div>

    // <div classNameNameNameName="caption text-center">
    //     <h1>3clickjobs</h1>
    //     <h3>Find best job offers with 3clickjobs</h3>
    //     <button type="button" classNameNameName="btn btn-primary btn-lg">Search Available Jobs</button>
    //     <button type="button" classNameNameName="btn btn-primary btn-lg">Search Available Employees</button><br></br>
    //     <button type="button" classNameNameName="btn btn-secondary btn-lg">Offer/Seek Job</button>
    //     </div>
        
    //     </div>


<>
<section class="intro carousel slide bg-overlay-light h-auto" id="carouselExampleCaptions">
    <ol class="carousel-indicators">
      <li data-target="#carouselExampleCaptions" data-slide-to="0" class="active"></li>
      <li data-target="#carouselExampleCaptions" data-slide-to="1" class=""></li>
    </ol>
    <div class="carousel-inner" role="listbox">
      <div class="carousel-item active">
        <img class="d-block img-fluid" alt="First slide" src="https://images.pexels.com/photos/838413/pexels-photo-838413.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"/>
        <div class="carousel-caption ">
          	<h2 class="display-4 text-white mb-2 mt-4">Lorem ipsum dolor sit amet, consectetur adipiscing elit</h2>
			<p class="text-white mb-3 px-5 lead">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do enim ad minim veniam, </p>
			<a href="#" class="btn btn-primary btn-capsul px-4 py-2">Search Available Jobs</a>
        </div>
      </div>
      <div class="carousel-item">
        <img class="d-block img-fluid" alt="First slide" src="https://www.talkgeo.com/wp-content/uploads/Business-Travel-1.jpg"/>
        <div class="carousel-caption ">
          	<h2 class="display-4 text-white mb-2 mt-4">Lorem ipsum dolor sit amet, consectetur adipiscing elit</h2>
			<p class="text-white mb-3 px-5 lead">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do enim ad minim veniam, </p>
			<a href="#" class="btn btn-primary btn-capsul px-4 py-2">Explore More</a>
        </div>
      </div>
    </div>
    <a class="carousel-control-prev" href="#carouselExampleCaptions" role="button" data-slide="prev">
      <span class="carousel-control-prev-icon" aria-hidden="true"></span>
      <span class="sr-only">Previous</span>
    </a>
    <a class="carousel-control-next" href="#carouselExampleCaptions" role="button" data-slide="next">
      <span class="carousel-control-next-icon" aria-hidden="true"></span>
      <span class="sr-only">Next</span>
    </a>
</section>

<div class="jumbotron">
  <p> text</p>
  </div>
  </>
    


    );
  }
}
export default Home;