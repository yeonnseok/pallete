import React, { ReactElement } from 'react';
import { LoadingOutlined } from '@ant-design/icons';
import { Spin } from 'antd';


export const LoadingSpinner = (): ReactElement => {

  const antIcon = (
    <LoadingOutlined
      style={{ fontSize: 50 }}
      spin
    />
  );

  return (
    <Spin
      indicator={antIcon}
      style={{
        marginTop: 'calc(50vh - 30px)',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
      }}
    />
  );
};
