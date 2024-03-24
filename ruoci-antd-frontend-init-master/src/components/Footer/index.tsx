import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import '@umijs/max';
import React from 'react';

const Footer: React.FC = () => {
  const defaultMessage = '=========SCAU_MIS============';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'repository',
          title: (
            <>
              <GithubOutlined /> github仓库
            </>
          ),
          href: 'https://github.com/linruoci',
          blankTarget: true,
        },
        {
          key: 'Ant Design',
          title: 'ant design',
          href: 'https://ant-design.antgroup.com/index-cn',
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
