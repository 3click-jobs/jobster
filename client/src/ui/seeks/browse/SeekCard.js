import React from 'react';
// import { Link } from 'react-router-dom'
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
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
// import MoreVertIcon from '@material-ui/icons/MoreVert';
import RemoveCircleSharp from '@material-ui/icons/RemoveCircleSharp';
import CheckCircleSharp from '@material-ui/icons/CheckCircleSharp';
import ClickAwayListener from '@material-ui/core/ClickAwayListener';
import Tooltip from '@material-ui/core/Tooltip';


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
        alignSelf: 'flex-end',
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
    // flexGrow: 1,
    height: '100%',
  },
  // paper: {
  //   padding: theme.spacing(2),
  //   margin: 'auto',
  //   maxWidth: 500,
  // },
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
  avatar: {
    backgroundColor: blueGrey[500],
    '& a, & a:hover, & a:visited': {
      textDecoration: 'none',
      color: 'white'
    }
  },
}));

export const SeekCard = (props) => {
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
                {/* <Link to='/offer'>S</Link> */}
                S
              </Avatar>
            }
            action={
              <React.Fragment>
                <Tooltip title={ props.role==="ROLE_GUEST" ? "Must be sign in!" : "Contact" } aria-label="contact">
                  <span>
                    <IconButton aria-label="accept" onClick={ () => props.handleAcceptSeek(props.seek) } disabled={props.role==="ROLE_GUEST" ? true : false} >
                      <CheckCircleSharp style={{ color: green[500], fontSize: 40 }} />
                    </IconButton>
                  </span>
                </Tooltip>
                <Tooltip title={ props.role==="ROLE_GUEST" ? "Must be sign in!" : "Remove" } aria-label="remove">
                  <span>
                    <IconButton aria-label="decline" onClick={ () => props.handleDeclineSeek(props.seek) } disabled={props.role==="ROLE_GUEST" ? true : false} >
                      <RemoveCircleSharp style={{ color: red[500], fontSize: 40 }} />
                    </IconButton>
                  </span>
                </Tooltip>
                {/* <IconButton aria-label="settings">
                  <MoreVertIcon />
                </IconButton> */}
              </React.Fragment>
            }
            title={props.seek.jobType.jobTypeName}
            subheader={"Price per hour: " + props.seek.price + "€"}
          />
          <CardContent>
            <Typography gutterBottom variant="subtitle1">Details and/or skills for a job:</Typography>
            <Typography variant="body2" gutterBottom color="textSecondary">
                {props.seek.detailsLink}
            </Typography>
          </CardContent>
          <CardActions disableSpacing>
          <Tooltip title={ props.role==="ROLE_GUEST" ? "Must be sign in!" : "Rate" } aria-label="rate">
              <span>
                <IconButton aria-label="add to favorites" disabled={props.role==="ROLE_GUEST" ? true : false} >
                  <StarRate />
                </IconButton>
              </span>
            </Tooltip>
            <Tooltip title={ props.role==="ROLE_GUEST" ? "Must be sign in!" : "Comment" } aria-label="comment">
              <span>
                <IconButton aria-label="share" disabled={props.role==="ROLE_GUEST" ? true : false} >
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
              aria-label="show seek details"
            >
              <ExpandMoreIcon />
            </IconButton>
          </CardActions>
          <Collapse in={expanded} timeout="auto" unmountOnExit>
            <CardContent>
              <Typography gutterBottom variant="subtitle1">Acceptable location/s for jobs:</Typography>
              <Typography variant="body2" gutterBottom color="textSecondary">
                {props.seek.distanceToJob>0 ? 
                    "Up to " + props.seek.distanceToJob + " km from " + props.seek.city + ", " + props.seek.country
                    : "In " + props.seek.city + ", " + props.seek.country}
              </Typography>
              <Typography gutterBottom variant="subtitle1"> {props.seek.flexibileDates ? "Acceptable period (FLEXIBLE) for jobs: " : "Acceptable period (NOT FLEXIBLE) for jobs: "}</Typography>
                <Typography variant="body2" gutterBottom color="textSecondary">
                  {props.seek.endDate ? "From " + props.seek.beginningDate + " to " + props.seek.endDate : "From " + props.seek.beginningDate}
              </Typography>
              <Typography gutterBottom variant="subtitle1"> {props.seek.listJobDayHoursPostDto ? props.seek.flexibileDays ? "Acceptable day/s (FLEXIBLE) and day period/s for jobs:" : "Acceptable day/s (NOT FLEXIBLE) and day period/s for jobs:" : null} </Typography>
                { props.seek.listJobDayHoursPostDto 
                  ? props.seek.listJobDayHoursPostDto.map(day => {
                    return <Typography key={day.id} variant="body2" gutterBottom color="textSecondary">
                      {day.isMinMax ? day.day.substr(4) + ": min " + day.fromHour + " and max " + day.toHour + " hours in a day" : day.day.substr(4) + ": from " + day.fromHour + " to " + day.toHour + " o'clock" } 
                      {day.flexibileHours ? " (FLEXIBLE)" : " (NOT FLEXIBLE)"}
                    </Typography>
                  }) 
                  : null             
                }
              <Typography gutterBottom variant="subtitle1">Employee:</Typography>
              <Typography variant="body2" gutterBottom color="textSecondary">
                Name: {props.seek.employee.firstName + " " + props.seek.employee.lastName}
              </Typography>
              <Typography variant="body2" gutterBottom color="textSecondary">
                Location: {props.seek.employee.city + ", " + props.seek.employee.country}
              </Typography>
              <Typography variant="body2" gutterBottom color="textSecondary">
                About: {props.seek.employee.about}
              </Typography>
              <Typography variant="body2" gutterBottom color="textSecondary">
                E-mail: {props.seek.employee.email}
              </Typography>
              <Typography variant="body2" gutterBottom color="textSecondary">
                Phone: {props.seek.employee.mobilePhone}
              </Typography>
            </CardContent>
          </Collapse>
        </Card>
      </ClickAwayListener>
    </ThemeProvider>
  )
}

export default SeekCard