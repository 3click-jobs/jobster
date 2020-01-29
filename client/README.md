## Tips for running on your local machine

### `npm install`

If the project will not compile the most likely culprit is a missing package. Run `npm install` to install the missing packages.

Packages added to the project at the latest revision:

[prop-types](https://github.com/facebook/prop-types)
[react-styleguidist](https://github.com/styleguidist/react-styleguidist)

### Environment settings

If there are problems with establishing a connection to the backend check the `env.js` file inside src > common > api. Change the `baseUrl` variable to fit your environment.

### Documentation browsing and editing

You can browse the auto-generated documentation by running the `npx styleguidist server` command and opening your browser at `http://localhost:6060/` (or some other port, depending on the styleguidist settings).

You can contribute to the documentation effort very easily, following these simple steps:

1. For any component you want to document, put `import PropTypes from 'prop-types'` at the top of the file, in order to import the `prop-types` module.
2. Write a short summary above the component definition.
3. Document properties near the end of the file, following the examples in the previously documented files.

Further instructions & tips can be found at the [StyleGuidist documentation](https://react-styleguidist.js.org/).  


*legacy stuff:*

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## Available Scripts

*From the default Create React App readme file, the most important bits left her for reference.*

In the project directory, you can run:

### `npm start`

Runs the app in the development mode.<br />
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br />
You will also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.<br />
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.<br />
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br />
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.
