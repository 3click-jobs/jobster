import React from 'react';
import { makeStyles, createMuiTheme, ThemeProvider } from '@material-ui/core/styles';
import clsx from 'clsx';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
// import CardMedia from '@material-ui/core/CardMedia';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Collapse from '@material-ui/core/Collapse';
import Avatar from '@material-ui/core/Avatar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import { red, green, blueGrey } from '@material-ui/core/colors';
// import FavoriteIcon from '@material-ui/icons/Favorite';
// import ShareIcon from '@material-ui/icons/Share';
import AddComment from '@material-ui/icons/AddComment';
// import NoteAdd from '@material-ui/icons/NoteAdd';
import StarRate from '@material-ui/icons/StarRate';
// import RemoveCircleSharp from '@material-ui/icons/RemoveCircleSharp';
// import CheckCircleSharp from '@material-ui/icons/CheckCircleSharp';
import ClearIcon from '@material-ui/icons/Clear';
import DoneSharpIcon from '@material-ui/icons/DoneSharp';
// import Check from '@material-ui/icons/Check';
// import Clear from '@material-ui/icons/Clear';
import ClickAwayListener from '@material-ui/core/ClickAwayListener';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import Tooltip from '@material-ui/core/Tooltip';
// import MoreVertIcon from '@material-ui/icons/MoreVert';
// import { Link } from 'react-router-dom'


const theme = createMuiTheme({
  overrides: {
    MuiCardHeader: {
      root: {
        padding: '2px 16px',
      },
      avatar: {
        marginRight: '10px',
      },
      action: {
        alignSelf: 'auto',
        marginTop: '0px',
        marginRight: '-12px',
      },
    },
    // MuiButtonBase: {
    //   root: {
    //     padding: '6px 12px',
    //   },
    // },
    MuiIconButton: {
      root: {
        padding: '6px 4px',
      },
    },
    MuiCardContent: {
      root: {
        padding: '2px 16px',
      }
    },
    MuiCardActions: {
      root: {
        padding: '2px',
      }
    },
  },
});

const useStyles = makeStyles(theme => ({
  card: {
    // maxWidth: 345,
    height: '100%',
  },
  media: {
    height: 0,
    paddingTop: '56.25%', // 16:9
  },
  expand: {
    transform: 'rotate(0deg)',
    marginLeft: 'auto',
    transition: theme.transitions.create('transform', {
      duration: theme.transitions.duration.shortest,
    }),
  },
  expandOpen: {
    transform: 'rotate(180deg)',
  },
  // avatarDecline: {
  //   backgroundColor: red[500],
  //   '& a, & a:hover, & a:visited': {
  //     textDecoration: 'none',
  //     color: 'white'
  //   }
  // },
  // avatarAccept: {
  //   backgroundColor: blueGrey[500],
  //   '& a, & a:hover, & a:visited': {
  //     textDecoration: 'none',
  //     color: 'white'
  //   }
  // },
  avatar: {
    backgroundColor: blueGrey[500],
    '& a, & a:hover, & a:visited': {
      textDecoration: 'none',
      color: 'white'
    }
  },
}));

export const OfferCard = (props) => {
  const classes = useStyles();
  const [expanded, setExpanded] = React.useState(false);

  const handleExpandClick = () => {
    setExpanded(!expanded);
  };

  const handleClickAway = () => {
    setExpanded(false);
  };

  return (
    <ThemeProvider theme={theme}>
      <ClickAwayListener onClickAway={handleClickAway}>
        <Card className={classes.card}>
          <CardHeader
            avatar={
              <Avatar aria-label="recipe" className={classes.avatar}>
                {/* <Link to='/apply'>O</Link> */}
                O
              </Avatar>
            }
            action={
              <React.Fragment>
                <Tooltip title={ props.role==="ROLE_GUEST" ? "Must be sign in!" : "Apply" } aria-label="apply">
                  <span>
                    <IconButton aria-label="accept" onClick={ () => props.handleAcceptOffer(props.offer) } disabled={props.role==="ROLE_GUEST" ? true : false} >
                      <DoneSharpIcon style={{ color: green[900], fontSize: 30 }} />
                    </IconButton>
                  </span>
                </Tooltip>
                {/* <span style={ props.role==="ROLE_GUEST" ? { cursor: 'not-allowed' } : null } > */}
                <Tooltip title={ props.role==="ROLE_GUEST" ? "Must be sign in!" : "Remove" } aria-label="remove">
                  <span>
                    <IconButton aria-label="decline" onClick={ () => props.handleDeclineOffer(props.offer) } disabled={props.role==="ROLE_GUEST" ? true : false} >
                      <ClearIcon style={{ color: red[900], fontSize: 30 }}/>
                    </IconButton>
                  </span>
                </Tooltip>
                {/* </span> */}
                {/* <IconButton aria-label="settings">
                  <MoreVertIcon />
                </IconButton> */}
                {/* <Avatar aria-label="recipe" className={classes.avatarDecline}>
                  <Link to='/apply'><Clear /></Link>
                </Avatar> */}
              </React.Fragment>
            }
            title={props.offer.jobType.jobTypeName}
            subheader={"Price per hour: " + props.offer.price + "€"}
          />
          <CardContent>
            <Typography gutterBottom variant="subtitle1">Details about a job:</Typography>
            <Typography variant="body2" gutterBottom color="textSecondary">
                {props.offer.detailsLink}
            </Typography>
          </CardContent>
          <CardActions disableSpacing>
            <Tooltip title={ props.role==="ROLE_GUEST" ? "Must be sign in!" : "Rate" } aria-label="rate">
              <span>
                <IconButton disabled={props.role==="ROLE_GUEST" ? true : false} aria-label="Rate offer">
                  <StarRate />
                </IconButton>
              </span>
            </Tooltip>
            <Tooltip title={ props.role==="ROLE_GUEST" ? "Must be sign in!" : "Comment" } aria-label="comment">
              <span>
                <IconButton disabled={props.role==="ROLE_GUEST" ? true : false} aria-label="Add comment">
                  <AddComment />
                </IconButton>
              </span>
            </Tooltip>
            <IconButton
              disabled={props.role==="ROLE_GUEST" ? true : false} 
              className={clsx(classes.expand, {
                [classes.expandOpen]: expanded,
              })}
              onClick={handleExpandClick}
              aria-expanded={expanded}
              aria-label="Show offer details"
            >
              <ExpandMoreIcon />
            </IconButton>
          </CardActions>
          <Collapse in={expanded} timeout="auto" unmountOnExit>
            <CardContent>
              <Typography gutterBottom variant="subtitle1">Job location:</Typography>
              <Typography variant="body2" gutterBottom color="textSecondary">
                {props.offer.city + ", " + props.offer.country}
              </Typography>
              <Typography gutterBottom variant="subtitle1">Number of needed employees: {props.offer.numberOfEmployees}</Typography>
              <Typography gutterBottom variant="subtitle1">{props.offer.flexibileDates ? "Job period (FLEXIBLE):" : "Job period (NOT FLEXIBLE):"}</Typography>
              <Typography variant="body2" gutterBottom color="textSecondary">
                {props.offer.endDate ? "From " + props.offer.beginningDate + " to " + props.offer.endDate : "From " + props.offer.beginningDate}
              </Typography>
              <Typography gutterBottom variant="subtitle1"> {props.offer.listJobDayHoursPostDto ? props.offer.flexibileDays ? "Day/s (FLEXIBLE) and day period/s for job:" : "Day/s (NOT FLEXIBLE) and day period/s for job:" : null} </Typography>
                { props.offer.listJobDayHoursPostDto 
                  ? props.offer.listJobDayHoursPostDto.map(day => {
                    return <Typography key={day.id} variant="body2" gutterBottom color="textSecondary">
                      {day.isMinMax ? day.day.substr(4) + ": min " + day.fromHour + " and max " + day.toHour + " hours in a day" : day.day.substr(4) + ": from " + day.fromHour + " to " + day.toHour + " o'clock" } 
                      {day.flexibileHours ? " (FLEXIBLE)" : " (NOT FLEXIBLE)"}
                    </Typography>
                  }) 
                  : null             
                }
              <Typography gutterBottom variant="subtitle1">Employer:</Typography>
              <Typography variant="body2" gutterBottom color="textSecondary">
                Name: {props.offer.employer.companyName ? props.offer.employer.companyName : props.offer.employer.firstName + " " + props.offer.employer.lastName }
              </Typography>
              <Typography variant="body2" gutterBottom color="textSecondary">
                Place of residence: {props.offer.employer.city + ", " + props.offer.employer.country}
              </Typography>
              <Typography variant="body2" gutterBottom color="textSecondary">
                About: {props.offer.employer.about}
              </Typography>
              <Typography variant="body2" gutterBottom color="textSecondary">
                E-mail: {props.offer.employer.email}
              </Typography>
              <Typography variant="body2" gutterBottom color="textSecondary">
                Phone: {props.offer.employer.mobilePhone}
              </Typography>
            </CardContent>
          </Collapse>
        </Card>
      </ClickAwayListener>
    </ThemeProvider>
  )
}

export default OfferCard