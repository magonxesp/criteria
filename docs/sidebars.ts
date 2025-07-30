import type {SidebarsConfig} from '@docusaurus/plugin-content-docs';

// This runs in Node.js - Don't use client-side code here (browser APIs, JSX...)

/**
 * Creating a sidebar enables you to:
 - create an ordered group of docs
 - render a sidebar for each doc of that group
 - provide next/previous navigation

 The sidebars can be generated from the filesystem, or explicitly defined here.

 Create as many sidebars as you want.
 */
const sidebars: SidebarsConfig = {
  tutorialSidebar: [
    'index',
    {
      type: 'category',
      label: 'Getting Started',
      link: {
        type: 'doc',
        id: 'getting-started/index'
      },
      items: [
        'getting-started/spring-data-jpa',
        'getting-started/mongodb'
      ],
    },
    'serialization',
    {
      type: 'category',
      label: 'Development',
      link: {
        type: 'doc',
        id: 'development/index'
      },
      items: [
        'development/install-git-cliff'
      ],
    },
    {
      type: 'link',
      label: 'Reference',
      href: 'https://magonxesp.github.io/criteria/reference/index.html'
    }
  ],
};

export default sidebars;
