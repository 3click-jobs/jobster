import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import clsx from 'clsx';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardMedia from '@material-ui/core/CardMedia';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Collapse from '@material-ui/core/Collapse';
import Avatar from '@material-ui/core/Avatar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import { red } from '@material-ui/core/colors';
// import FavoriteIcon from '@material-ui/icons/Favorite';
// import ShareIcon from '@material-ui/icons/Share';
import AddComment from '@material-ui/icons/AddComment';
// import NoteAdd from '@material-ui/icons/NoteAdd';
import StarRate from '@material-ui/icons/StarRate';


import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import { Link } from 'react-router-dom'

const useStyles = makeStyles(theme => ({
  card: {
    // maxWidth: 345,
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
  avatar: {
    backgroundColor: red[500],
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

  return (
    <Card className={classes.card}>
      <CardHeader
        avatar={
          <Avatar aria-label="recipe" className={classes.avatar}>
            <Link to='/apply'>OK</Link>
          </Avatar>
        }
        action={
          <IconButton aria-label="settings">
            <MoreVertIcon />
          </IconButton>
        }
        title={props.offer.jobType.jobTypeName}
        subheader={"In " + props.offer.city + ", " + props.offer.country + " (" + props.offer.beginningDate + " - " + props.offer.endDate + ")"}
      />
      <CardContent>
        <Typography gutterBottom variant="subtitle1">{props.offer.employer.companyName}</Typography>
        <Typography variant="body2" gutterBottom color="textSecondary">
          {props.offer.employer.city + ", " + props.offer.employer.country}
        </Typography>
        <Typography gutterBottom variant="subtitle1">Details about a job:</Typography>
        <Typography variant="body2" gutterBottom color="textSecondary">
            {props.offer.detailsLink}
        </Typography>
        <Typography gutterBottom variant="subtitle1">Price per hour: {props.offer.price}€</Typography>
        <Typography gutterBottom variant="subtitle1">Number of employees: {props.offer.numberOfEmployees}</Typography>
      </CardContent>
      <CardActions disableSpacing>
        <IconButton aria-label="add to favorites">
          <StarRate />
        </IconButton>
        <IconButton aria-label="share">
          <AddComment />
        </IconButton>
        <IconButton
          className={clsx(classes.expand, {
            [classes.expandOpen]: expanded,
          })}
          onClick={handleExpandClick}
          aria-expanded={expanded}
          aria-label="show more"
        >
          <ExpandMoreIcon />
        </IconButton>
      </CardActions>
      <Collapse in={expanded} timeout="auto" unmountOnExit>
        <CardContent>
          <Typography gutterBottom variant="subtitle1">E-mail: {props.offer.employer.email}</Typography>
          <Typography gutterBottom variant="subtitle1">Phone: {props.offer.employer.mobilePhone}</Typography>
        </CardContent>
      </Collapse>
    </Card>
  )
}

export default OfferCard