﻿module Pan3d {
    export class Display3D extends Object3D {
        public objData: ObjData;
        public program: WebGLProgram;
        public shader: Shader3D;
        public beginTime: number;
        public type: number; //类型  
        protected _onStage: boolean;
        public sceneVisible: boolean = true;
        protected _hasDestory: boolean = false;
        public _scene: Pan3d.SceneManager;
        constructor() {
            super();
            this._onStage = false;
        }

        public update(): void {

        }

        public get onStage(): boolean {
            return this._onStage;
        }

        public addStage(): void {
            this._onStage = true;
        }

        public removeStage(): void {
            this._onStage = false;
        }
        public resize(): void {

        }

        public destory(): void {
            if (this.objData) {
                this.objData.useNum--;
            }
        }

        public static distance(v1: any, v2: any): number {
            var x1: number = v1.x - v2.x;
            var y1: number = v1.y - v2.y;
            var z1: number = v1.z - v2.z;
            return Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
        }
    }
}