import React from 'react'
import { makeStyles } from '@material-ui/core/styles';
import NavigationRoundedIcon from '@material-ui/icons/NavigationRounded';
import Fab from '@material-ui/core/Fab';
import Tooltip from '@material-ui/core/Tooltip';

const useStyles = makeStyles(theme => ({
    // absolute: {
    //   position: 'absolute',
    //   bottom: theme.spacing(2),
    //   right: theme.spacing(3),
    // },
    floatingBottomRight : {
      margin: 0,
      top: 'auto',
      right: 20,
      bottom: 20,
      left: 'auto',
      position: 'fixed',
      overflowX: 'hidden',
    },
  }));


export const ToTopButton = () => {

    const [windowScrolled, setWindowScrolled] = React.useState(false)

    const classes = useStyles();

    
    React.useEffect(() => {
        function handleScroll() {
          if ( (window.pageYOffset === 0 && windowScrolled) || (window.pageYOffset !== 0 && !windowScrolled ) )
            setWindowScrolled(!windowScrolled);
            // windowScrolled = window.pageYOffset;
        }
    
        window.addEventListener('scroll', handleScroll);
        return function cleanup() {
          window.removeEventListener('scroll', handleScroll);
        };
      });

      
    return (
        <React.Fragment>
        { windowScrolled && 
            <Tooltip title="Top" aria-label="top" placement="right-end" onClick={ () => { window.scroll(0, 0); }}>
              {/* <IconButton aria-label="delete"> */}
              <Fab color="primary" visibility="hidden" className={classes.floatingBottomRight} >
                <NavigationRoundedIcon />
              </Fab>
              {/* </IconButton> */}
            </Tooltip>
          }
        </React.Fragment>
    )
}

export default ToTopButton
