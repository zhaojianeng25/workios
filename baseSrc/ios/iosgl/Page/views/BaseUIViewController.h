//
//  BaseUIViewController.h
//  iosgl
//
//  Created by zhao on 2/12/2019.
//  Copyright © 2019 zhao. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface BaseUIViewController : UIViewController
@property (nonatomic, strong) UIView                       *statusBarView;//IPX系列特殊遮罩
@property (nonatomic, strong) UIView                       *winBg;

 
/**
 UI创建
 */
- (void)initFWUI NS_REQUIRES_SUPER;

@end

NS_ASSUME_NONNULL_END
