const path = require('path');
const CracoLessPlugin = require('craco-less');
const { CracoAliasPlugin } = require('react-app-alias');
const SpeedMeasurePlugin = require('speed-measure-webpack-plugin');

const resolve = (arg) => path.resolve(__dirname, arg);
const smp = new SpeedMeasurePlugin();

module.exports = {
  plugins: [
    {
      plugin: CracoLessPlugin,
      options: {
        lessLoaderOptions: {
          lessOptions: {
            modifyVars: {
              '@primary-color': '#35C5F0',
              '@link-color': '#35C5F0',
              '@error-color': '#FF7777',
              '@border-radius-base': '4px',
            },
            javascriptEnabled: true,
          },
        },
      },
    },
    {
      plugin: CracoAliasPlugin,
      options: {},
    },
  ],
  webpack: smp.wrap({
    configure: (webpackConfig) => {
      webpackConfig.module.rules.push({
        test: /\.js$/,
        include: /node_modules/,
        use: [{
          loader: "babel-loader",
          options: {
            presets: [
              ["@babel/preset-env", {
                modules: false,
                forceAllTransforms: true,
                useBuiltIns: false,
              }],
            ],
          },
        }],
      })
      return webpackConfig;
    },
    alias: {
      assets: resolve('src/assets'),
      components: resolve('src/components'),
			common: resolve('src/common'),
      models: resolve('src/models'),
      pages: resolve('src/pages'),
      contexts: resolve('src/contexts'),
      utils: resolve('src/utils'),
      hooks: resolve('src/hooks'),
      api: resolve('src/api'),
    },
  }),
};


