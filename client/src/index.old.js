import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom'
import App from './App';
import {JobProvider} from './context';

ReactDOM.render((
    <JobProvider>
    <BrowserRouter>
      <App />
    </BrowserRouter>
    </JobProvider>
  ), document.getElementById('root'))