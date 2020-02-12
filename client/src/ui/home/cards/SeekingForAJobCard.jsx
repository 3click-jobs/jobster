import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardMedia from '@material-ui/core/CardMedia';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Avatar from '@material-ui/core/Avatar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import { red } from '@material-ui/core/colors';
import FavoriteIcon from '@material-ui/icons/Favorite';
import ShareIcon from '@material-ui/icons/Share';
import { Link } from 'react-router-dom'
import { useTranslation } from 'react-i18next'

const useStyles = makeStyles(theme => ({
  card: {
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
  avatar: {
    backgroundColor: red[500],
    '& a, & a:hover, & a:visited': {
      textDecoration: 'none',
      color: 'white'
    }
  },
}));

export const SeekingForAJobCard = () => {
  const classes = useStyles();
  const { t } = useTranslation();
  
  return (
    <Card className={classes.card}>
      <CardHeader
        avatar={
          <Avatar aria-label="recipe" className={classes.avatar}>
            <Link to='/seek'>
              {t('card.avatarGo')}
              {/* GO */}
            </Link>
          </Avatar>
        }
        title={t('seekingForAJobCard.headerTitle')}
        subheader={t('seekingForAJobCard.subheader')}
        // title="Seek for a job in three clicks"
        // subheader="Find the way"
      />
      <CardMedia
        className={classes.media}
        image="/img/carousel-3.jpg"
        title={t('seekingForAJobCard.mediaTitle')}
        // title="Seek for job"
      />
      <CardContent>
        <Typography variant="body2" color="textSecondary" component="p">
          {t('seekingForAJobCard.content')}
          {/* Set the location. Set the job type and the job description. Check if everything is all right and post the job seek. As easy as that. */}
        </Typography>
      </CardContent>
      <CardActions disableSpacing>
        <IconButton 
          aria-label={t('card.actionFavorites')}
          // aria-label="add to favorites"
        >
          <FavoriteIcon />
        </IconButton>
        <IconButton 
          aria-label={t('card.actionShare')}
          // aria-label="share"
        >
          <ShareIcon />
        </IconButton>
      </CardActions>
    </Card>
  )
}

export default SeekingForAJobCard